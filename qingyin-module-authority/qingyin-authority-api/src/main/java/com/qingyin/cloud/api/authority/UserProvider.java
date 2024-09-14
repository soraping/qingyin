package com.qingyin.cloud.api.authority;

import com.qingyin.cloud.api.authority.vo.UserVo;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = ApiConstants.USER_NAME, path = ApiConstants.USER_PREFIX, contextId = ApiConstants.USER_NAME)
public interface UserProvider {
    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @PostMapping("/detail/id")
    CommonResponse<UserVo> getUserDetailById(Long id);
}
