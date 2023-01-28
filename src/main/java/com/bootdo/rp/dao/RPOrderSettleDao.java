package com.bootdo.rp.dao;

import com.bootdo.rp.domain.RPOrderSettleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 应收、应付票据结算表
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Mapper
public interface RPOrderSettleDao {

    RPOrderSettleDO get(Integer id);

    List<RPOrderSettleDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(RPOrderSettleDO orderSettle);

    int update(RPOrderSettleDO orderSettle);

    int remove(Integer id);

    int saveBatch(List<RPOrderSettleDO> orderEntryList);

    int delete(Map<String, Object> map);
}
