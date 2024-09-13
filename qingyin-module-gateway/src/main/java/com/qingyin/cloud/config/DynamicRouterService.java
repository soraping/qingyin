package com.qingyin.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <h1>事件推送 Aware: 动态更新路由网关 Service</h1>
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class DynamicRouterService implements ApplicationEventPublisherAware {

    /** 事件发布 */
    private ApplicationEventPublisher publisher;

    /** 写路由定义 */
    private final RouteDefinitionWriter routeDefinitionWriter;
    /** 获取路由定义 */
    private final RouteDefinitionLocator routeDefinitionLocator;

    public DynamicRouterService(RouteDefinitionWriter routeDefinitionWriter, RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 完成事件推送句柄的初始化
        this.publisher = applicationEventPublisher;
    }

    /**
     * <h2>增加路由定义</h2>
     * @param definition
     */
    public String add(RouteDefinition definition) {
        log.info("gateway add route: [{}]", definition);
        // 保存路由配置并发布
        this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        // 发布事件通知给 gateway， 同步新增的路由定义
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    /**
     * <h2>更新路由</h2>
     * */
    public String updateList(List<RouteDefinition> definitions) {

        log.info("gateway update route: [{}]", definitions);

        // 先拿到当前 Gateway 中存储的路由定义
        List<RouteDefinition> routeDefinitionsExits =
                routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
            // 清除掉之前所有的 "旧的" 路由定义
            routeDefinitionsExits.forEach(rd -> {
                log.info("delete route definition: [{}]", rd);
                del(rd.getId());
            });
        }

        // 把更新的路由定义同步到 gateway 中
        definitions.forEach(definition -> updateByRouteDefinitionId(definition));
        return "success";
    }

    /**
     * <h2>根据路由 id 删除路由配置</h2>
     * @param id
     * @return
     */
    private String del(String id) {
        try {
            log.info("gateway del route id: [{}]", id);
            this.routeDefinitionWriter.delete(Mono.just(id));
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        } catch (Exception ex) {
            log.error("gateway del route fail: [{}]", ex.getMessage(), ex);
            return "del fail";
        }
    }

    /**
     * <h2>更新指定路由</h2>
     * 更新的实现策略比较简单: 删除 + 新增 = 更新
     * @param routeDefinition
     * @return
     */
    private String updateByRouteDefinitionId(RouteDefinition routeDefinition) {
        try {
            log.info("gateway update route: [{}]", routeDefinition);
            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        }catch (Exception ex) {
            return "update fail, not find route id: " + routeDefinition.getId();
        }

        try {
            this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        }catch (Exception ex) {
            return "update route fail";
        }
    }

}
