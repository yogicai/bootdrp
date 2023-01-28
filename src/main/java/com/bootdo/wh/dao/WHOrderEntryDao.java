package com.bootdo.wh.dao;

import com.bootdo.wh.domain.WHOrderEntryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 入库出库商品
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
@Mapper
public interface WHOrderEntryDao {

    WHOrderEntryDO get(Integer id);

    List<WHOrderEntryDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(WHOrderEntryDO orderEntry);

    int update(WHOrderEntryDO orderEntry);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    int saveBatch(List<WHOrderEntryDO> orderEntryList);

    int delete(Map<String, Object> map);
}
