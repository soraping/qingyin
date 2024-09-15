package com.qingyin.cloud.api.authority.dto;

import com.qingyin.cloud.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginReqDto extends BaseDto {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "用户密码不能为空")
    private String password;
}
