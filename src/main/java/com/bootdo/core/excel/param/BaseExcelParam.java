package com.bootdo.core.excel.param;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.bootdo.core.excel.enums.VerifyResultEnum;
import lombok.Data;

/**
 * 导入订单
 * IExcelDataModel负责设置行号，IExcelModel 负责设置错误信息
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class BaseExcelParam implements IExcelDataModel, IExcelModel {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 用于区分空行、列值校验不过
     */
    private VerifyResultEnum verifyResultEnum;


    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
