package com.qingyin.cloud.service;

import com.qingyin.cloud.api.authority.vo.UserVo;
import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.api.authority.dto.UserSearchDto;
import com.qingyin.cloud.vo.PageResult;

public interface IUserService {
    User getUserDetail(UserSearchDto userSearchValidate);

    PageResult<UserVo> queryUserPageList(UserSearchDto userSearchValidate);
}
