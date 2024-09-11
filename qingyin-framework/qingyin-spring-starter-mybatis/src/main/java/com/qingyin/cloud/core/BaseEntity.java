package com.qingyin.cloud.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer isDelete;

    private Long createTime;

    private Long updateTime;

    private Long deleteTime;
}
