package com.bootdo.modular.engage.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.modular.engage.result.BalanceItemResult;
import com.bootdo.modular.engage.result.EntryBalanceResult;
import com.bootdo.modular.engage.result.EntryBalanceSumResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
public interface ProductBalanceDao {

    List<BalanceItemResult> pBalance(@Param("param")Map<String, Object> map);

    Page<EntryBalanceResult> pBalanceEntry(Page<EntryBalanceResult> page, @Param("param")Map<String, Object> params);

    EntryBalanceSumResult pBalanceEntryCountSum(@Param("param")Map<String, Object> map);

}
