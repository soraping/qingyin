package com.qingyin.cloud.validate.User;

import com.qingyin.cloud.validate.BaseValidate;
import lombok.Data;

@Data
public class LoginValidate extends BaseValidate {
    private String username;
    private String password;
}
