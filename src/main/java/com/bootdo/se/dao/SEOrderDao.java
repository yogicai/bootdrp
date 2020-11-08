package com.bootdo.se.dao;

import com.bootdo.se.domain.SEOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 购货订单
 * @author yogiCai
 * @email 1992lcg@163.com
 * @date 2018-02-18 16:50:26
 */
@Mapper
public interface SEOrderDao {

    SEOrderDO get(Integer id);

    List<SEOrderDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SEOrderDO order);

    int update(Map<String, Object> map);

    int audit(Map<String, Object> map);

    int remove(Integer id);

    int batchRemove(String[] ids);

    int delete(Map<String, Object> map);
}
