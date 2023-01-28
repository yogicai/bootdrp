package com.bootdo.rp.dao;

import com.bootdo.rp.domain.RPOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 收付款单
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Mapper
public interface RPOrderDao {

    RPOrderDO get(Integer id);

    List<RPOrderDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int countRP(Map<String, Object> map);

    int save(RPOrderDO order);

    int saveBatch(List<RPOrderDO> pointEntryList);

    int audit(Map<String, Object> params);

    int delete(Map<String, Object> map);
}
