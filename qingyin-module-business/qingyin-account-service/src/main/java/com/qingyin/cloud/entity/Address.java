package com.qingyin.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Address extends BaseEntity {
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    /**
     * 所属用户
     */
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
}
