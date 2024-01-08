package com.bootdo.modular.engage.dao;

import com.bootdo.modular.engage.result.EntryBalanceResult;

import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
public interface ProductBalanceDao {

    List<Map<String, Object>> pBalance(Map<String, Object> map);

    List<EntryBalanceResult> pBalanceEntry(Map<String, Object> map);

    Map<String, Object> pBalanceEntryCountSum(Map<String, Object> map);

}
