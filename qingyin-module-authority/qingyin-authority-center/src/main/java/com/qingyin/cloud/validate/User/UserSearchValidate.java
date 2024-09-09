package com.qingyin.cloud.validate.User;

import com.qingyin.cloud.validate.BaseValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchValidate extends BaseValidate {
    private Long id;
    private String username;
}
