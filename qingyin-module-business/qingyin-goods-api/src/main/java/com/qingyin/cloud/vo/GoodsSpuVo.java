package com.qingyin.cloud.vo;

import lombok.Data;

import java.util.List;

/**
 * 商品信息
 */
@Data
public class GoodsSpuVo extends BaseVo{
    private Long id;

    private String name;

    private Long ownerId;

    private List<GoodsSpecVo> goodsSpecVoList;
}
