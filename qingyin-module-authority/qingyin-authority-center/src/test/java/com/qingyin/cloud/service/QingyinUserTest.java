package com.qingyin.cloud.service;

import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.mapper.UserMapper;
import com.qingyin.cloud.utils.TimeUtils;
import com.qingyin.cloud.utils.ToolUtils;
import com.qingyin.cloud.validate.User.LoginValidate;
import com.qingyin.cloud.validate.User.RegisterValidate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class QingyinUserTest {

    @Resource
    private ILoginService loginService;

    @Resource
    private UserMapper userMapper;

    @Test
    public void saveUser(){
        User user = new User();
        user.setUsername("hahaha");
        String salt = ToolUtils.randomString(6);
        user.setSalt(salt);
        user.setPassword(ToolUtils.makeMd5("123456" + salt));
        user.setCreateTime(TimeUtils.timestamp());
        user.setUpdateTime(TimeUtils.timestamp());
        userMapper.insert(user);
    }

    @Test
    public void registerTest() throws Exception {
        RegisterValidate registerValidate = new RegisterValidate();
        registerValidate.setUsername("vicici");
        registerValidate.setPassword("123456");
        String token = loginService.register(registerValidate);
        log.info("user register token = [{}]", token);
    }

    @Test
    public void loginTest() throws Exception {
        LoginValidate loginValidate = new LoginValidate();
        loginValidate.setUsername("vicici");
        loginValidate.setPassword("123456");
        String token = loginService.login(loginValidate);
        log.info("user login token = [{}]", token);
    }

}
