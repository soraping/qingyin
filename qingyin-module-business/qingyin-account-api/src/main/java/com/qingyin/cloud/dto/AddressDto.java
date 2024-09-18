package com.qingyin.cloud.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddressDto extends BaseDto{
    private Long id;
    @NotNull(message = "手机号不能为空")
    private String phoneNumber;
    @NotNull(message = "收货人不能为空")
    private String consignee;
    private String province;
    private String city;
    private String addressDetail;
}
