package com.bootdo.se.dao;

import com.bootdo.se.domain.SEOrderEntryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */
@Mapper
public interface SEOrderEntryDao {

    SEOrderEntryDO get(Integer id);

    List<SEOrderEntryDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SEOrderEntryDO orderEntry);

    int update(SEOrderEntryDO orderEntry);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    int saveBatch(List<SEOrderEntryDO> orderEntryList);

    int delete(Map<String, Object> map);
}
