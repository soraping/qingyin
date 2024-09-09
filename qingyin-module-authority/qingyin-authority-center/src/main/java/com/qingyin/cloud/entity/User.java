package com.qingyin.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qingyin.cloud.core.BaseEntity;
import lombok.Data;

@Data
public class User extends BaseEntity {

    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String salt;

}
