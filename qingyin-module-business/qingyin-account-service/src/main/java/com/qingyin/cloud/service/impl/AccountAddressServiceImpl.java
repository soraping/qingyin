package com.qingyin.cloud.service.impl;

import com.qingyin.cloud.dto.AddressDto;
import com.qingyin.cloud.entity.Address;
import com.qingyin.cloud.filter.AccessContext;
import com.qingyin.cloud.mapper.AddressMapper;
import com.qingyin.cloud.service.IAccountAddressService;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AccountAddressServiceImpl implements IAccountAddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    public void add(AddressDto addressDto) {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        Address address = new Address();
        address.setOwnerId(loginUserInfo.getId());
        BeanUtils.copyProperties(addressDto, address);
        addressMapper.insert(address);
    }
}
