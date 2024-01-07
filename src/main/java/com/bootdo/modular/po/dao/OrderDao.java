package com.bootdo.modular.po.dao;

import com.bootdo.modular.po.domain.OrderDO;

import java.util.List;
import java.util.Map;

/**
 * 购货订单
 *
 * @author yogiCai
 * @date 2017-11-28 21:30:03
 */
public interface OrderDao {

    OrderDO get(Integer id);

    List<OrderDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(OrderDO order);

    int update(Map<String, Object> map);

    int audit(Map<String, Object> map);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    int delete(Map<String, Object> map);
}