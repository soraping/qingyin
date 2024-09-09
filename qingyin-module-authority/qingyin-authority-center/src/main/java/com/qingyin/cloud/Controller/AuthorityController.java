package com.qingyin.cloud.Controller;

import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.service.ILoginService;
import com.qingyin.cloud.validate.User.LoginValidate;
import com.qingyin.cloud.validate.User.RegisterValidate;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <h1>对外暴露的授权服务接口</h1>
 * */
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @Resource
    private ILoginService loginService;

    /**
     * <h2>注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户</h2>
     * */
    @PostMapping("/register")
    public CommonResponse<String> register(@RequestBody RegisterValidate registerValidate) throws Exception {
        return CommonResponse.success(ErrorEnum.SUCCESS.getMsg(), loginService.register(registerValidate));
    }

    /**
     * <h2>用户登录，返回用户 token </h2>
     * */
    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody LoginValidate loginValidate) throws Exception {
        return CommonResponse.success(ErrorEnum.SUCCESS.getMsg(), loginService.login(loginValidate));
    }

}
