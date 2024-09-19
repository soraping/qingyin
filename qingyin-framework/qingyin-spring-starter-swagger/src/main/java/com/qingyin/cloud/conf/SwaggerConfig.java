package com.qingyin.cloud.conf;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>Swagger 配置类</h1>
 * 原生: /swagger-ui.html
 * 美化: /doc.html
 * */
@Configuration
public class SwaggerConfig {

    // 此处为模块化配置，将API文档配置成几个模块，添加每个模块名，此次模块下所有API接口的统一前缀
    @Bean
    public GroupedOpenApi AccountApi()
    {
        return GroupedOpenApi.builder().group("用户API").pathsToMatch("//qingyin-rpc-api/account/**").build();
    }


    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("openapi接口文档")
                        .description("qingyin业务接口文档")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                        .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));

    }
}
