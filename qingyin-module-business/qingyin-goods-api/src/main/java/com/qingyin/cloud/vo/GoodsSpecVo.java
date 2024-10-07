package com.qingyin.cloud.vo;

import lombok.Data;

/**
 * 规格
 */
@Data
public class GoodsSpecVo extends BaseVo{
    private Long id;

    private Long spuId;

    private String specKey;

    private String specValues;
}
