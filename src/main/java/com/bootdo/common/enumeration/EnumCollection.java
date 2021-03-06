package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
* @Author: yogiCai
* @Date: 2018-01-21 11:45:46
*/
public class EnumCollection {
    //单据类型
    public static final EnumSet<BillType> BILL_TYPE = EnumSet.of(BillType.CG_ORDER, BillType.TH_ORDER, BillType.XS_ORDER, BillType.CW_SK_ORDER, BillType.CW_FK_ORDER, BillType.WH_RK_ORDER, BillType.WH_CK_ORDER);
    //采购单状态
    public static final EnumSet<OrderStatus> ORDER_CG_STATUS = EnumSet.of(OrderStatus.WAITING_PAY, OrderStatus.PART_PAY, OrderStatus.FINISH_PAY, OrderStatus.ORDER_CANCEL);
    //销售单状态
    public static final EnumSet<OrderStatus> ORDER_XS_STATUS = EnumSet.of(OrderStatus.WAITING_PAY, OrderStatus.PART_PAY, OrderStatus.FINISH_PAY, OrderStatus.ORDER_CANCEL);

    //订单审核状态
    public static final EnumSet<AuditStatus> AUDIT_STATUS = EnumSet.of(AuditStatus.YES, AuditStatus.NO);
    //类目分类
    public static final EnumSet<CategoryType> CATEGORY_TYPE = EnumSet.of(CategoryType.CUSTOMER, CategoryType.VENDOR, CategoryType.PRODUCT, CategoryType.PAYMENT, CategoryType.INCOME, CategoryType.ACCOUNT);

    //积分记录状态
    public static final EnumSet<PointStatus> POINT_STATUS = EnumSet.of(PointStatus.NORMAL, PointStatus.DISABLE, PointStatus.SETTLE);
    //积分记录来源
    public static final EnumSet<PointSource> POINT_SOURCE = EnumSet.of(PointSource.ORDER, PointSource.RETURN, PointSource.MANUAL);

    //成本类型
    public static final EnumSet<CostType> COST_TYPE = EnumSet.of(CostType.PO_CG, CostType.PO_TH, CostType.WH_CK, CostType.WH_RK);

    /**
     * 表格列展示用
     */
    public static Map<String, EnumSet> listEnum() {
        Map<String, EnumSet> setMap = Maps.newHashMap();
        setMap.put("BILL_TYPE", BILL_TYPE);
        setMap.put("ORDER_CG_STATUS", ORDER_CG_STATUS);
        setMap.put("ORDER_XS_STATUS", ORDER_XS_STATUS);
        setMap.put("CATEGORY_TYPE", CATEGORY_TYPE);
        setMap.put("AUDIT_STATUS", AUDIT_STATUS);
        setMap.put("POINT_STATUS", POINT_STATUS);
        setMap.put("POINT_SOURCE", POINT_SOURCE);
        setMap.put("COST_TYPE", COST_TYPE);
        return setMap;
    }

    /**
    * 下拉列表用：[{AUDIT_STATUS : [{YES: 已审核}, {NO: 未审核}]}]
    */
    public static Map<String, List<Map<String, String>>> listEnumMap() {
        Map<String, List<Map<String, String>>> setMap = Maps.newHashMap();
        setMap.put("BILL_TYPE", convertEnumMap(BILL_TYPE));
        setMap.put("ORDER_CG_STATUS", convertEnumMap(ORDER_CG_STATUS));
        setMap.put("ORDER_XS_STATUS", convertEnumMap(ORDER_XS_STATUS));
        setMap.put("CATEGORY_TYPE", convertEnumMap(CATEGORY_TYPE));
        setMap.put("AUDIT_STATUS", convertEnumMap(AUDIT_STATUS));
        setMap.put("POINT_STATUS", convertEnumMap(POINT_STATUS));
        setMap.put("POINT_SOURCE", convertEnumMap(POINT_SOURCE));
        setMap.put("COST_TYPE", convertEnumMap(COST_TYPE));
        return setMap;
    }

    private static List<Map<String, String>> convertEnumMap(EnumSet<?> enumSet) {
        List<Map<String, String>> list = Lists.newArrayList();
        for (Enum e : enumSet) {
            EnumBean eb = (EnumBean)e;
            list.add(ImmutableMap.of("name", eb.remark(), "value", eb.name()));
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(listEnum(), SerializerFeature.QuoteFieldNames));
    }

}
