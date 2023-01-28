package com.bootdo.rp.dao;

import com.bootdo.rp.domain.RPOrderEntryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 应收、应付票据核销目标单据
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Mapper
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
