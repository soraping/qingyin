package com.qingyin.cloud.api.authority;

import com.qingyin.cloud.api.authority.dto.UserLoginReqDto;
import com.qingyin.cloud.api.authority.dto.UserRegisterReqDto;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.AUTH_NAME, path = ApiConstants.AUTH_PREFIX, contextId = ApiConstants.AUTH_NAME)
public interface AuthorityProvider {

    @PostMapping( "/register")
    CommonResponse<String> register(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto) throws Exception;

    @PostMapping("/token")
    CommonResponse<String> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto) throws Exception;

}
