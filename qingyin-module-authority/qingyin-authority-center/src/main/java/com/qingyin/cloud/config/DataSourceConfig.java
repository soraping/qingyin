package com.qingyin.cloud.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /**
     * 默认数据源
     * @return
     */
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.qingyin-authority-db")
    public DataSource getPrimaryDataSource()
    {
        return new DruidDataSource();
    }

}
