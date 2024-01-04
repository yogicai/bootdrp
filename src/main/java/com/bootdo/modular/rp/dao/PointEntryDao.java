package com.bootdo.modular.rp.dao;

import com.bootdo.modular.rp.domain.PointEntryDO;

import java.util.List;
import java.util.Map;

/**
 * 客户积分
 *
 * @author yogiCai
 * @date 2018-03-06 23:17:49
 */
public interface PointEntryDao {

    PointEntryDO get(Integer id);

    List<PointEntryDO> list(Map<String, Object> map);

    List<PointEntryDO> listG(Map<String, Object> map);

    int count(Map<String, Object> map);

    int countG(Map<String, Object> map);

    int save(PointEntryDO pointEntry);

    int saveBatch(List<PointEntryDO> pointEntryList);

    int update(PointEntryDO pointEntry);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    int delete(Map<String, Object> map);
}
