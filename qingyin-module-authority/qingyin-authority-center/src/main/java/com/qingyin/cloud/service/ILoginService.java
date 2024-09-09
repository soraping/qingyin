package com.qingyin.cloud.service;

import com.qingyin.cloud.validate.User.LoginValidate;
import com.qingyin.cloud.validate.User.RegisterValidate;

public interface ILoginService {
    String login(LoginValidate loginValidate) throws Exception;
    String register(RegisterValidate registerValidate) throws Exception;
}
