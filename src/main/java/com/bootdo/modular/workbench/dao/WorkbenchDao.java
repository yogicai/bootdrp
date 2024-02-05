package com.bootdo.modular.workbench.dao;

import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
public interface WorkbenchDao {

    List<Map<String, Object>> pHisPBillTrend(Map<String, Object> map);

    List<Map<String, Object>> pBillTrend(Map<String, Object> map);

    List<Map<String, Object>> pBillTrendPie(Map<String, Object> map);

    List<Map<String, Object>> pCashTrend(Map<String, Object> map);
}
