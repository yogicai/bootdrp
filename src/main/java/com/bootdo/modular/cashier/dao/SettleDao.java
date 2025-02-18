package com.bootdo.modular.cashier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.cashier.result.JournalGeneralResult.SettleOrderItem;
import com.bootdo.modular.rp.domain.RPOrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 核销收款
 *
 * @author yogiCai
 * @since 2018-07-14 22:31:58
 */
public interface SettleDao extends BaseMapper<RPOrderDO> {

    /**
     * 年份核销金额合计
     */
    List<Map<String, Object>> flowSettleYear(@Param("param") Map<String, Object> param);

    /**
     * 订单核销明细
     */
    List<SettleOrderItem> generalSettleOrderItem(@Param("param") Map<String, Object> param);

}
