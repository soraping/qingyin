package com.qingyin.cloud.api;

import com.qingyin.cloud.dto.AddressDto;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.vo.AddressVo;
import com.qingyin.cloud.vo.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = ApiConstants.FEIGN_VALUE, path = ApiConstants.ACCOUNT_ADDRESS_PREFIX, contextId = ApiConstants.ADDRESS_NAME)
@Tag(name = "RPC 服务 - 用户收件地址")
public interface AccountAddressProvider {

    /**
     * 新增地址
     * @param addressDto
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户收件地址")
    CommonResponse<Object> addAddress(@Valid @RequestBody AddressDto addressDto);

    /**
     * 地址列表
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "用户收件地址列表")
    CommonResponse<List<AddressVo>> getAddressList();

}
