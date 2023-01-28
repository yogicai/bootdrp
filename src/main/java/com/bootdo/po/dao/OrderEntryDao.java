package com.bootdo.po.dao;

import com.bootdo.po.domain.OrderEntryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @date 2018-01-21 12:38:44
 */
@Mapper
public interface OrderEntryDao {

    OrderEntryDO get(Integer id);

    List<OrderEntryDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(OrderEntryDO orderEntry);

    int update(OrderEntryDO orderEntry);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    int saveBatch(List<OrderEntryDO> orderEntryList);

    int delete(Map<String, Object> map);
}
