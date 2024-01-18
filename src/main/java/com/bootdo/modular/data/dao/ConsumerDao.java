package com.bootdo.modular.data.dao;


import com.bootdo.modular.data.domain.ConsumerDO;

import java.util.List;
import java.util.Map;

/**
 * 客户信息表
 *
 * @author yogiCai
 * @date 2017-11-18 22:41:14
 */
public interface ConsumerDao {

    ConsumerDO get(Integer id);

    List<ConsumerDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ConsumerDO consumer);

    int update(ConsumerDO consumer);

    int remove(Integer id);

    int batchRemove(Integer[] ids);
}
