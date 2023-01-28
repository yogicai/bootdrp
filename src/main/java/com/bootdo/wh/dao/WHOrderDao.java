package com.bootdo.wh.dao;

import com.bootdo.wh.domain.WHOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
@Mapper
public interface WHOrderDao {

    WHOrderDO get(Integer id);

    List<WHOrderDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(WHOrderDO order);

    int update(WHOrderDO order);

    int audit(Map<String, Object> map);

    int remove(Integer id);

    int batchRemove(String[] ids);

    int delete(Map<String, Object> map);
}
