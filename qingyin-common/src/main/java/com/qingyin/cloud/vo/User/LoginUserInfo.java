package com.qingyin.cloud.vo.User;

import com.qingyin.cloud.vo.BaseVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>用户登录信息</h1>
 * 用于存储token内
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo extends BaseVo {
    private Long id;
    private String username;
}
