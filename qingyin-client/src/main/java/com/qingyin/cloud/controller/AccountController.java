package com.qingyin.cloud.controller;

import com.qingyin.cloud.api.authority.AuthorityProvider;
import com.qingyin.cloud.api.authority.dto.UserRegisterReqDto;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AuthorityProvider authorityProvider;


    @PostMapping("/register")
    public CommonResponse<String> register(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto) throws Exception {
        return authorityProvider.register(userRegisterReqDto);
    }


}
