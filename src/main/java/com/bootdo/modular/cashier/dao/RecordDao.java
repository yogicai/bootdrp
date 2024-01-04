package com.bootdo.modular.cashier.dao;

import com.bootdo.modular.cashier.domain.RecordDO;

import java.util.List;
import java.util.Map;

/**
 * (CashierRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-25 19:39:09
 */
public interface RecordDao {

    RecordDO get(Integer id);

    List<RecordDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    Map<String,Object> selectSum(Map<String, Object> map);

    int save(RecordDO recordDO);

    int saveBatch(List<RecordDO> list);

    int update(RecordDO journal);

    int remove(Map<String, Object> map);

    int batchRemove(Integer[] ids);

    List<RecordDO> multiSelect(Map<String, Object> map);
}

