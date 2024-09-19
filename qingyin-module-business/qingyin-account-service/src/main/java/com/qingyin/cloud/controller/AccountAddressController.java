package com.qingyin.cloud.controller;

import com.qingyin.cloud.api.AccountAddressProvider;
import com.qingyin.cloud.dto.AddressDto;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.service.IAccountAddressService;
import com.qingyin.cloud.vo.AddressVo;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(ApiConstants.ACCOUNT_ADDRESS_PREFIX)
@RestController
public class AccountAddressController implements AccountAddressProvider {

    @Resource
    private IAccountAddressService accountAddressService;

    @Override
    public CommonResponse<Object> addAddress(AddressDto addressDto) {
        accountAddressService.add(addressDto);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<List<AddressVo>> getAddressList() {
        return null;
    }
}
