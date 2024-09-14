package com.qingyin.cloud.api.authority.vo;

import com.qingyin.cloud.entity.BaseEntity;
import com.qingyin.cloud.util.TimeUtils;
import com.qingyin.cloud.vo.BaseVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserVo extends BaseVo {
    private Long id;
    private String username;
    private String createTime;
    private Integer isDisable;

    public static <T extends BaseEntity> UserVo cover (T data) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(data, userVo);
        userVo.setCreateTime(TimeUtils.timestampToDate(data.getCreateTime()));
        return userVo;
    }
}
