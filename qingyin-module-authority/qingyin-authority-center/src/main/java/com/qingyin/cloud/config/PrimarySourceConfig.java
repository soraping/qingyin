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

/**
 * admin 数据源配置
 * 在 MyBatis 中，SqlSessionFactory 和 SqlSessionTemplate 两者都可以用于创建 SqlSession 对象，但是它们之间有一些区别：
 *
 * SqlSessionFactory 是 MyBatis 的核心接口，用于创建 SqlSession 对象。
 * SqlSessionTemplate 是 MyBatis 的工具类，封装了 SqlSession 的创建、关闭等操作，是线程安全的。
 * 由于 SqlSessionTemplate 内部封装了 SqlSessionFactory，所以它可以创建 SqlSession 对象，
 * 但是如果您同时使用了 SqlSessionFactory 和 SqlSessionTemplate，可能会导致冲突。
 *
 * 建议您只使用一个方式创建 SqlSession 对象，要么使用 SqlSessionFactory，要么使用 SqlSessionTemplate。
 */
@Configuration
//配置mapper路径
@MapperScan(
        basePackages = "com.qingyin.cloud.mapper",
// sqlSessionTemplateRef 和 sqlSessionFactoryRef 不能同时配置，sqlSessionTemplateRef已经包含了sqlSessionFactoryRef
//        sqlSessionFactoryRef = "qingyinAuthoritySqlSessionFactory",
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
//        bean.setMapperLocations(
//                // 设置mybatis的xml所在位置
//                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/admin/*.xml"));
        // 添加 MPJ 拦截器
        // https://mybatisplusjoin.com/pages/problem.html#invalid-bound-statement-not-found
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
