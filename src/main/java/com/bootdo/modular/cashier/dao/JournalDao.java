package com.bootdo.modular.cashier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.result.JournalGeneralResult.DebtItem;
import com.bootdo.modular.cashier.result.JournalGeneralResult.OperateItem;
import com.bootdo.modular.cashier.result.JournalGeneralResult.OperateMonthItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 日记账
 *
 * @author yogiCai
 * @date 2018-07-14 22:31:58
 */
public interface JournalDao extends BaseMapper<RecordDO> {

    /**
     * 销售概况
     */
    List<OperateItem> generalFlowYear(@Param("param") Map<String, Object> param);

    /**
     * 月销售概况
     */
    List<OperateMonthItem> generalFlowMonth(@Param("param") Map<String, Object> param);

    /**
     * 现金支出明细
     */
    List<Map<String, Object>> flowRecordList(@Param("param") Map<String, Object> param);

    /**
     * 欠款明细
     */
    List<DebtItem> debtRecordList(@Param("param") Map<String, Object> param);

}
