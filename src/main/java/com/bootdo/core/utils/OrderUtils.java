package com.bootdo.core.utils;

import cn.hutool.core.util.RandomUtil;
import com.bootdo.core.enums.BillType;

import java.util.Date;

/**
 * @Author: yogiCai
 */
public class OrderUtils {

    private static final String PO_CG_PRE = "CG";
    private static final String PO_CG_TK_PRE = "TH";
    private static final String PO_XS_PRE = "XS";
    private static final String CW_FK_PRE = "CWFK";
    private static final String CW_SK_PRE = "CWSK";
    private static final String WH_RK_PRE = "WHRK";
    private static final String WH_CK_PRE = "WHCK";

    /**
     * 采购单-退货单
     */
    public static String generateOrderNoCG(BillType billType) {
        if (BillType.CG_ORDER.equals(billType)) {
            return PO_CG_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
        } else {
            return PO_CG_TK_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
        }
    }
    /**
     * 销售单
     */
    public static String generateOrderNoXS() {
        return PO_XS_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
    }
    /**
    /**
     * 财务-付款单
     */
    public static String generateOrderNoCW(BillType billType) {
        if (BillType.CW_SK_ORDER.equals(billType)) {
            return CW_SK_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
        } else {
            return CW_FK_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
        }
    }

    /**
     * 仓库-出库单号-入库单号
     */
    public static String generateOrderNoWH(BillType billType) {
        if (BillType.WH_CK_ORDER.equals(billType)) {
            return WH_CK_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
        } else {
            return WH_RK_PRE + DateUtils.convertDateToStrISO(new Date()) + RandomUtil.randomNumbers(5);
        }
    }
}
