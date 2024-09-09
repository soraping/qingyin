package com.qingyin.cloud;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

@SpringBootTest
public class DBDocTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void buildDBDoc() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
    }

}
