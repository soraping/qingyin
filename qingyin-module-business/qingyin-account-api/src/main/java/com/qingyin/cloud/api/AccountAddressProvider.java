package com.qingyin.cloud.api;

import com.qingyin.cloud.dto.AddressDto;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.vo.AddressVo;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = ApiConstants.FEIGN_VALUE, path = ApiConstants.ADDRESS_PREFIX, contextId = ApiConstants.ADDRESS_NAME)
public interface AccountAddressProvider {

    /**
     * 新增地址
     * @param addressDto
     * @return
     */
    @PostMapping("/add")
    CommonResponse<Object> addAddress(@Valid @RequestBody AddressDto addressDto);

    /**
     * 地址列表
     * @return
     */
    @GetMapping("/list")
    CommonResponse<List<AddressVo>> getAddressList();

}
