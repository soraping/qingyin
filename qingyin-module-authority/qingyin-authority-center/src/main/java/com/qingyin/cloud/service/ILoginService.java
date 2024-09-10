package com.qingyin.cloud.service;

import com.qingyin.cloud.api.authority.dto.UserLoginReqDto;
import com.qingyin.cloud.api.authority.dto.UserRegisterReqDto;

public interface ILoginService {
    String login(UserLoginReqDto loginValidate) throws Exception;
    String register(UserRegisterReqDto registerValidate) throws Exception;
}
