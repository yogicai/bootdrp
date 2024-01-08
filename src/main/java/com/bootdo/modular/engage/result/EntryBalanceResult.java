package com.bootdo.modular.engage.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bootdo.core.enums.BillType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 商品成本表
 * 
 * @author yogiCai
 * @date 2018-03-17 19:35:03
 */
@Data
public class EntryBalanceResult {

    /** 单据日期 */
    @Excel(name = "单据日期", exportFormat = "yyyy-MM-dd", width = 15)
    private Date billDate;
    /** 单据编号 */
    @Excel(name = "单据编号", width = 25)
    private String billNo;
    /** 单据类型 */
    @Excel(name = "单据类型", enumExportField = "remark")
    private BillType billType;
    /** 商品ID */
    @Excel(name = "商品ID")
    private String entryId;
    /** 商品名称 */
    @Excel(name = "商品名称")
    private String entryName;
    /** 商品单位 */
    @Excel(name = "商品单位")
    private String entryUnit;
    /** 商品单价 */
    @Excel(name = "商品单价", numFormat = "#,##0.00", width = 15)
    private BigDecimal entryPrice;
    /** 成本单价 */
    @Excel(name = "库存变更", numFormat = "#,##0.00", width = 15)
    private BigDecimal costPrice;
    /** 库存变更 */
    @Excel(name = "库存变更")
    private BigDecimal totalQty;
    /** 创建时间 */
    @Excel(name = "创建时间", exportFormat = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date createTime;
    /** 修改时间 */
    @Excel(name = "修改时间", exportFormat = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date updateTime;

}
