package com.qingyin.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 商品 sku
 */
@Data
public class GoodsSku extends BaseEntity{
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    private Long spuId;

    private String skuName;

    /**
     * 规格ids
     */
    private String specIds;

    private String imgUrls;
}
