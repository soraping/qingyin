package com.qingyin.cloud.service;

import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.api.authority.dto.UserSearchDto;

public interface IUserService {
    User getUserDetail(UserSearchDto userSearchValidate);
}
