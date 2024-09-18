package com.qingyin.cloud.vo;

import com.qingyin.cloud.entity.BaseEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddressVo extends BaseVo{
    private Long id;
    private Long ownerId;
    /**
     * 收货电话
     */
    private String phoneNumber;

    /**
     * 收货人
     */
    private String consignee;

    private String province;
    private String city;
    private String addressDetail;

    public static <T extends BaseEntity> AddressVo cover(T data) {
        AddressVo addressVo = new AddressVo();
        BeanUtils.copyProperties(data, addressVo);
        return addressVo;
    }
}
