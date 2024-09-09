package com.qingyin.cloud.mapper;

import com.qingyin.cloud.core.IBaseMapper;
import com.qingyin.cloud.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends IBaseMapper<User> {
}
