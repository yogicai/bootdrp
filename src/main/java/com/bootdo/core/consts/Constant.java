package com.bootdo.core.consts;

/**
 * @author L
 */
public class Constant {
    //演示系统账户
    public static String DEMO_ACCOUNT = "test";
    //自动去除表前缀
    public static String AUTO_REOMVE_PRE = "true";
    //停止计划任务
    public static String STATUS_RUNNING_STOP = "stop";
    //开启计划任务
    public static String STATUS_RUNNING_START = "start";
    //通知公告阅读状态-未读
    public static String OA_NOTIFY_READ_NO = "0";
    //通知公告阅读状态-已读
    public static int OA_NOTIFY_READ_YES = 1;
    //部门根节点id
    public static Long DEPT_ROOT_ID = 0L;


    public static String HTML_BR = "</br>";

    public static String Q_DAY = "DAY";
    public static String Q_WEEK = "WEEK";
    public static String Q_MONTH = "MONTH";
    public static String Q_YEAR = "YEAR";

    /** 报表 */
    public static String R = "YEAR";

    /** main主页 */
    public static String MAIN_TAB_CUSTOMER = "CUSTOMER";
    public static String MAIN_TAB_PRODUCT = "PRODUCT";

    /** 提示消息 */
    public static String PO_RP_ORDER_REMARK = "系统自动生成付款单";
    public static String SE_RP_POINT_REMARK = "系统自动生成【%s】";
    public static String COST_REMARK = "%S【%s】";
    public static String IMPORT_ORDER_INVALID = "第{}行：{}";

}
