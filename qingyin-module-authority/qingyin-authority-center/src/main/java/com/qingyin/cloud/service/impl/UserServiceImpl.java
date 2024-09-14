package com.qingyin.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.mapper.UserMapper;
import com.qingyin.cloud.service.IUserService;
import com.qingyin.cloud.api.authority.dto.UserSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserDetail(UserSearchDto userSearchValidate) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getIsDelete, 0);
        if(StringUtils.isNotBlank(userSearchValidate.getUsername())){
            lambdaQueryWrapper.eq(User::getUsername, userSearchValidate.getUsername());
            return userMapper.selectOne(lambdaQueryWrapper);

        }else if(!Objects.isNull(userSearchValidate.getId())){
            return userMapper.selectById(userSearchValidate.getId());
        }

        return null;
    }
}
