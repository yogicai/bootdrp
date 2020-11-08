package com.bootdo.rp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 客户积分
 * 
 * @author yogiCai
 * @date 2018-03-06 23:17:49
 */
public class PointEntryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//客户ID
	private String consumerId;
	//客户名称
	private String consumerName;
	//积分来源
	private String source;
	//积分
	private BigDecimal point;
	//备注
	private String remark;
	//状态
	private String status;
    //关联单号
    private String relateNo;
    //计算积分的订单金额
    private BigDecimal totalAmount;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelateNo() {
        return relateNo;
    }

    public void setRelateNo(String relateNo) {
        this.relateNo = relateNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
