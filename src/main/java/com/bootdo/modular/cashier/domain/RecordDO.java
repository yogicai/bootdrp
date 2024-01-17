package com.bootdo.modular.cashier.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CashierRecord)实体类
 *
 * @author makejava
 * @since 2022-06-22 13:21:49
 */
@Data
public class RecordDO implements Serializable {

    private Long id;
    /**
     * 昵称
     */
    @Excel(name = "编号")
    private String nick;
    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    private String account;
    /**
     * 来源（微信、支付宝）
     */
    @Excel(name = "交易渠道")
    private String type;
    /**
     * 交易时间
     */
    @Excel(name = "交易时间", exportFormat = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date tradeTime;
    /**
     * 交易分类
     */
    @Excel(name = "交易分类")
    private String tradeClass;
    /**
     * 交易对方
     */
    @Excel(name = "交易对方", width = 20)
    private String targetName;
    /**
     * 对方账号
     */
    @Excel(name = "对方账号", width = 20)
    private String targetAccount;
    /**
     * 商品说明
     */
    @Excel(name = "商品说明", width = 20)
    private String tradeGoods;
    /**
     * 交易方式
     */
    @Excel(name = "交易方式")
    private String tradeType;
    /**
     * 资金用途
     */
    @Excel(name = "资金用途", width = 15)
    private String costType;
    /**
     * 金额(元)
     */
    @Excel(name = "金额(元)", numFormat = "#,##0.00", type = 10)
    private Double payAmount;
    /**
     * 收/支
     */
    @Excel(name = "收/支")
    private String payDirect;
    /**
     * 交易状态
     */
    @Excel(name = "交易状态")
    private String payStatus;
    /**
     * 交易订单号
     */
    @Excel(name = "交易订单号", width = 25)
    private String txnNo;
    /**
     * 商家订单号
     */
    @Excel(name = "商家订单号", width = 25)
    private String bizNo;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 20)
    private String remark;
    /**
     * 数据来源：对账导入 手工录入
     */
    @Excel(name = "数据来源")
    private String source;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", exportFormat = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date updateTime;

    /**
     * 设置手工录入固定属性
     */
    public RecordDO toManualRecord() {
        this.setNick(this.getAccount());
        this.setPayStatus("交易成功");
        this.setTradeClass("经营费用");
        this.setSource("手工录入");
        return this;
    }

}

