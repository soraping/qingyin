package com.qingyin.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class GoodsSpec extends BaseEntity {
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    private Long spuId;

    /**
     * 规格名
     */
    private String specName;

    /**
     * 规格值
     */
    private String specValue;

    /**
     * 规格图
     */
    private String imgUrl;
}
