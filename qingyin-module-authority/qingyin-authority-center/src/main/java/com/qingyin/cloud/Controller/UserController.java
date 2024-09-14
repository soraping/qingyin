package com.qingyin.cloud.Controller;

import com.qingyin.cloud.api.authority.UserProvider;
import com.qingyin.cloud.api.authority.dto.UserSearchDto;
import com.qingyin.cloud.api.authority.vo.UserVo;
import com.qingyin.cloud.enums.ApiConstants;
import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.service.IUserService;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(ApiConstants.USER_PREFIX)
public class UserController implements UserProvider {

    @Resource
    private IUserService userService;

    @Override
    public CommonResponse<UserVo> getUserDetailById(Long id) {
        UserSearchDto userSearchValidate = new UserSearchDto();
        userSearchValidate.setId(id);
        UserVo userVo = UserVo.cover(userService.getUserDetail(userSearchValidate));
        return CommonResponse.success(ErrorEnum.SUCCESS.getMsg(), userVo);
    }
}
