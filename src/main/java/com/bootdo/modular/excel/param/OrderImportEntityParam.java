package com.bootdo.modular.excel.param;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.bootdo.core.excel.param.BaseExcelParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 导入订单
 * IExcelDataModel负责设置行号，IExcelModel 负责设置错误信息
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class OrderImportEntityParam extends BaseExcelParam implements IExcelDataModel, IExcelModel {

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", width = 15)
    private String consumerName;

    /**
     * 商品名称
     */
    @NotBlank(message = "不能为空")
    @Excel(name = "商品名称", width = 15)
    private String entryName;

    /**
     * 数量
     */
    @NotNull(message = "不能为空")
    @Excel(name = "销售数量", width = 10)
    private BigDecimal totalQty;

    /**
     * 商品单价
     */
    @NotNull(message = "不能为空")
    @Excel(name = "零售价", width = 10)
    private BigDecimal entryPrice;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额", width = 10)
    private BigDecimal discountAmount;

    /**
     * 支付金额
     */
    @Excel(name = "支付金额", width = 10)
    private BigDecimal payAmount;

    /**
     * 纸质单据号
     */
    @Excel(name = "纸质单据号", width = 15)
    private String paperBillNo;

}
