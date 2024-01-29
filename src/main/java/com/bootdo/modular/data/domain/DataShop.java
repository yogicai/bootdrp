package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

/**
 * 店铺表
 *
 * @author L
 * @since 2024-01-26 15:39
 */
@TableName(value = "data_shop")
@Data
public class DataShop extends BaseEntity {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 店铺名称
     */
    private Integer no;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 店铺管理人员
     */
    private String managerId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}