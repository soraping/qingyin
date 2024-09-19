package com.qingyin.cloud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "RPC 服务 - 用户收件地址 Request DTO")
public class AddressDto extends BaseDto{
    private Long id;
    @NotNull(message = "手机号不能为空")
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
    @NotNull(message = "收货人不能为空")
    @Schema(description = "收货人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String consignee;
    private String province;
    private String city;
    private String addressDetail;
}
