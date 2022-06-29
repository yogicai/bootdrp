package com.bootdo.cashier.dao;

import com.bootdo.cashier.domain.JournalDO;
import com.bootdo.cashier.domain.RecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * (CashierRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-25 19:39:09
 */
@Mapper
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

