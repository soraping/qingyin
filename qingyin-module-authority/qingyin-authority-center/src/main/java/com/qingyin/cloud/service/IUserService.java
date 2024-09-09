package com.qingyin.cloud.service;

import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.validate.User.UserSearchValidate;

public interface IUserService {
    User getUserDetail(UserSearchValidate userSearchValidate);
}
