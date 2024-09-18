package com.qingyin.cloud.controller;

import com.qingyin.cloud.api.authority.AuthorityProvider;
import com.qingyin.cloud.api.authority.dto.UserLoginReqDto;
import com.qingyin.cloud.api.authority.dto.UserRegisterReqDto;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.service.ILoginService;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <h1>对外暴露的授权服务接口</h1>
 * */
@RestController
@RequestMapping(ApiConstants.AUTH_PREFIX)
public class AuthorityController implements AuthorityProvider {

    @Resource
    private ILoginService loginService;

    /**
     * <h2>注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户</h2>
     * */
    @Override
    public CommonResponse<String> register(UserRegisterReqDto userRegisterReqDto) throws Exception {
        return CommonResponse.success(ErrorEnum.SUCCESS.getMsg(), loginService.register(userRegisterReqDto));
    }

    /**
     * <h2>用户登录，返回用户 token </h2>
     * */
    @Override
    public CommonResponse<String> login(UserLoginReqDto userLoginReqDto) throws Exception {
        return CommonResponse.success(ErrorEnum.SUCCESS.getMsg(), loginService.login(userLoginReqDto));
    }
}
