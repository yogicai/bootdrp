package com.bootdo.modular.cashier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.result.ReconcileResult.ReconcileItem;
import com.bootdo.modular.rp.domain.RPOrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 
 * 收款对账
 * 
 * @author L
 * @since 2025-03-19 11:24
 */
public interface ReconcileDao extends BaseMapper<RPOrderDO> {

    Page<ReconcileItem> list(IPage<RecordDO> page, @Param("param") Map<String, Object> param);

    List<ReconcileItem> list(@Param("param") Map<String, Object> param);

    Map<String, Object> selectSum(@Param("param") Map<String, Object> map);

}
