package com.bootdo.modular.rp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 客户积分
 *
 * @author yogiCai
 * @since 2018-03-06 23:17:49
 */
@TableName(value = "rp_point_entry")
@Data
public class PointEntryDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 客户ID
     */
    private String consumerId;
    /**
     * 客户名称
     */
    private String consumerName;
    /**
     * 积分来源
     */
    private String source;
    /**
     * 积分
     */
    private BigDecimal point;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private String status;
    /**
     * 关联单号
     */
    private String relateNo;
    /**
     * 计算积分的订单金额
     */
    private BigDecimal totalAmount;

}
