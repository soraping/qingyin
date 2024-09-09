package com.qingyin.cloud.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.exception.LoginException;
import com.qingyin.cloud.mapper.UserMapper;
import com.qingyin.cloud.service.IJwtService;
import com.qingyin.cloud.service.ILoginService;
import com.qingyin.cloud.service.IUserService;
import com.qingyin.cloud.utils.ToolUtils;
import com.qingyin.cloud.validate.User.LoginValidate;
import com.qingyin.cloud.validate.User.RegisterValidate;
import com.qingyin.cloud.validate.User.UserSearchValidate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private IUserService userService;

    @Resource
    private IJwtService jwtService;

    @Override
    public String login(LoginValidate loginValidate) throws Exception {
        User user = userService.getUserDetail(new UserSearchValidate(null, loginValidate.getUsername()));
        if(Objects.isNull(user)){
            throw new LoginException(ErrorEnum.LOGIN_ACCOUNT_ERROR.getCode(), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }
        String salt = user.getSalt();
        String loginPassword = ToolUtils.makeMd5(loginValidate.getPassword() + salt);
        if(!StringUtils.equals(user.getPassword(), loginPassword)){
            throw new LoginException(ErrorEnum.LOGIN_ACCOUNT_ERROR.getCode(), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }

        String token = jwtService.generateToken(loginValidate.getUsername(), loginValidate.getPassword());

        return token;
    }

    @Override
    public String register(RegisterValidate registerValidate) throws Exception {
        User user = userService.getUserDetail(new UserSearchValidate(null, registerValidate.getUsername()));
        if(!Objects.isNull(user)){
            throw new LoginException(ErrorEnum.LOGIN_UNIQUE_ERROR.getCode(), ErrorEnum.LOGIN_UNIQUE_ERROR.getMsg());
        }
        User saveUser = new User();
        saveUser.setUsername(registerValidate.getUsername());
        String salt = ToolUtils.randomString(6);
        String password = ToolUtils.makeMd5(registerValidate.getPassword() + salt);
        saveUser.setSalt(salt);
        saveUser.setPassword(password);

        UserMapper userMapper = SpringUtil.getBean(UserMapper.class);
        userMapper.insert(saveUser);

        String token = jwtService.generateToken(registerValidate.getUsername(), registerValidate.getPassword());

        return token;
    }
}
