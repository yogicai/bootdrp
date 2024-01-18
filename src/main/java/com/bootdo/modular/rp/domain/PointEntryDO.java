package com.bootdo.modular.rp.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 客户积分
 *
 * @author yogiCai
 * @date 2018-03-06 23:17:49
 */
@Data
public class PointEntryDO implements Serializable {
    private static final long serialVersionUID = 1L;

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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}
