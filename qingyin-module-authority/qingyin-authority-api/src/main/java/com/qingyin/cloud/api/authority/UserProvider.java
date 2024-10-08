package com.qingyin.cloud.api.authority;

import com.qingyin.cloud.api.authority.dto.UserSearchDto;
import com.qingyin.cloud.api.authority.vo.UserVo;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.vo.CommonResponse;

import com.qingyin.cloud.vo.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApiConstants.FEIGN_VALUE, path = ApiConstants.USER_PREFIX, contextId = ApiConstants.USER_NAME)
public interface UserProvider {
    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    CommonResponse<UserVo> getUserDetailById( @RequestParam("id") Long id );

    @GetMapping("/list")
    PageResult<UserVo> getUserList(UserSearchDto userSearchDto);
}
