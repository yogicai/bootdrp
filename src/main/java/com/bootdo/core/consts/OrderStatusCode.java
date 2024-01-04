package com.bootdo.core.consts;

/**
 * code规则：700 + 模块编号（00-99） + 序号（000-999）
 * common(00)、create（01）、cancel（02）、update（03）、payment（04）、shipment（05）、refund（06）、rights（07）、search（08）、settle（09）
 * purse(20)、shop(21), wpOrder(22)
 *
 * @Author: yogiCai
 * @Date: 2018-02-04 10:23:43
 */
public class OrderStatusCode {

    public static final Integer ERROR = 500;
    public static final Integer ORDER_INVALID = 70010000;
    public static final Integer ORDER_PROCESS = 70010100;
    public static final Integer DATA_INVALID = 70020000;

}
