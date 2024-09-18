package com.qingyin.cloud.config;


import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.yulichang.interceptor.MPJInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
//配置mapper路径
@MapperScan(
        basePackages = "com.qingyin.cloud.mapper",
        sqlSessionTemplateRef = "qingyinAuthoritySessionTemplate"
)
public class PrimarySourceConfig {

    @Resource
    @Qualifier("MybatisGlobalConfig")
    private GlobalConfig globalConfig;

    @Resource
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Bean(name = "qingyinAuthoritySqlSessionFactory")
    // 表示这个数据源是默认数据源
    @Primary
    // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
    public SqlSessionFactory qingyinAuthoritySqlSessionFactory()
            throws Exception
    {
        // 分页插件
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(primaryDataSource);
        bean.setPlugins(mybatisPlusInterceptor, new MPJInterceptor());
        bean.setGlobalConfig(globalConfig);
        return bean.getObject();
    }

    @Bean("qingyinAuthoritySessionTemplate")
    // 表示这个数据源是默认数据源
    @Primary
    public SqlSessionTemplate qingyinAuthoritySessionTemplate(
            @Qualifier("qingyinAuthoritySqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager1() {
        return new DataSourceTransactionManager(primaryDataSource);
    }

}
