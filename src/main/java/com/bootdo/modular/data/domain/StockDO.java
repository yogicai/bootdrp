package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;


/**
 * 仓库表
 *
 * @author yogiCai
 * @since 2018-02-18 16:23:32
 */
@TableName(value = "data_stock")
@Data
public class StockDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 仓库编号
     */
    private String stockNo;
    /**
     * 仓库名称
     */
    private String stockName;
    /**
     * 仓库地址
     */
    private String stockAddress;
    /**
     * 状态
     */
    private Integer status = 0;

}
