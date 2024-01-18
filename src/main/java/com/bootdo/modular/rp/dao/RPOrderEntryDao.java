package com.bootdo.modular.rp.dao;

import com.bootdo.modular.rp.domain.RPOrderEntryDO;

import java.util.List;
import java.util.Map;

/**
 * 应收、应付票据核销目标单据
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
public interface RPOrderEntryDao {

    RPOrderEntryDO get(Integer id);

    List<RPOrderEntryDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(RPOrderEntryDO orderEntry);

    int update(RPOrderEntryDO orderEntry);

    int remove(Integer id);

    int saveBatch(List<RPOrderEntryDO> orderEntryList);

    int delete(Map<String, Object> map);
}
