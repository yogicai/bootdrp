package com.bootdo.modular.po.dao;

import com.bootdo.modular.po.domain.OrderEntryDO;

import java.util.List;
import java.util.Map;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @date 2018-01-21 12:38:44
 */
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
