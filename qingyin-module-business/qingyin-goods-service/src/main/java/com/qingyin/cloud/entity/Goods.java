package com.qingyin.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 商品 spu
 */
@Data
public class Goods extends BaseEntity {
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    private String name;

    private String imgUrls;
}
