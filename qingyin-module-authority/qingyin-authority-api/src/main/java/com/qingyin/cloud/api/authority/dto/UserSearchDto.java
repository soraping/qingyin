package com.qingyin.cloud.api.authority.dto;

import com.qingyin.cloud.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto extends BaseDto {
    private Long id;
    private String username;
}
