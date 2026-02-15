package com.bootdo.modular.workbench.dao;

import com.bootdo.modular.workbench.result.BillTrendItem;
import com.bootdo.modular.workbench.result.BillTrendPieItem;
import com.bootdo.modular.workbench.result.CashTrendItem;
import com.bootdo.modular.workbench.result.HisPBillTrendItem;

import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
public interface WorkbenchDao {

    List<HisPBillTrendItem> pHisBillTrend(Map<String, Object> map);

    List<BillTrendItem> pBillTrend(Map<String, Object> map);

    List<BillTrendPieItem> pBillTrendPie(Map<String, Object> map);

    List<CashTrendItem> pCashTrend(Map<String, Object> map);
}
