package com.qingyin.cloud.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * <h1>通过 nacos 下发动态路由配置, 监听 Nacos 中路由配置变更</h1>
 * */
@Slf4j
@Component
@DependsOn({ "gatewayConfig" })
public class DynamicRouteServiceByNacos {
    private ConfigService configService;

    @Resource
    private DynamicRouterService dynamicRouterService;

    /**
     * <h2>Bean 在容器中构造完成之后会执行 init 方法</h2>
     */
    @PostConstruct
    public void init() {
        log.info("gateway route init...");
        try {
            // 初始化 nacos 配置客户端
            configService = initConfigService();
            if(null == configService){
                log.error("init config service fail");
                return;
            }
            // 通过 Nacos Config 并指定路由配置路径去获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );
            log.info("get nacos current gateway config: [{}]", configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            if(CollectionUtils.isNotEmpty(definitionList)){
                for(RouteDefinition routeDefinition : definitionList) {
                    log.info("init gateway config: [{}]", routeDefinition.toString());
                    // 动态配置路由
                    dynamicRouterService.add(routeDefinition);
                }
            }

        }catch (Exception ex) {
            log.error("gateway route init has some error: [{}]", ex.getMessage(), ex);
        }

        // 开启监听器
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP);

    }

    /**
     * <h2>初始化 Nacos Config</h2>
     * */
    private ConfigService initConfigService() {

        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            properties.setProperty("username", GatewayConfig.NACOS_USERNAME);
            properties.setProperty("password", GatewayConfig.NACOS_PASSWORD);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception ex) {
            log.error("init gateway nacos config error: [{}]", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * <h2>监听 nacos 动态路由配置文件变化</h2>
     * @param dataId
     * @param group
     */
    private void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {

                /**
                 * <h2>自己提供线程池执行操作</h2>
                 * */
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 * <h2>监听器收到配置热更新</h2>
                 * @param configInfo Nacos 中最新的配置定义
                 * */
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("start to update config: [{}]", configInfo);
                    List<RouteDefinition> updateRouteDefinitions = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("update route: [{}]", updateRouteDefinitions.toString());
                    // 更新路由
                    dynamicRouterService.updateList(updateRouteDefinitions);
                }
            });
        } catch (NacosException ex) {
            log.error("dynamic update gateway config error: [{}]", ex.getMessage(), ex);
        }
    }
}
