package com.qingyin.cloud.api.authority.dto;

import com.qingyin.cloud.dto.PageParamDto;
import lombok.Data;

@Data
public class UserSearchDto extends PageParamDto {
    private Long id;
    private String username;
}
