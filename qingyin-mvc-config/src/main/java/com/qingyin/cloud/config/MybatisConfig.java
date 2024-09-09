package com.qingyin.cloud.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.github.yulichang.injector.MPJSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    @Bean(name = "MybatisGlobalConfig")
    public GlobalConfig globalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setTablePrefix("qy_");
        globalConfig.setDbConfig(dbConfig);
        // 注入MPJ
        // https://mybatisplusjoin.com/pages/problem.html#invalid-bound-statement-not-found
        globalConfig.setSqlInjector(new MPJSqlInjector());
        return globalConfig;
    }
}
