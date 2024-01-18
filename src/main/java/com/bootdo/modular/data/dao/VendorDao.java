package com.bootdo.modular.data.dao;

import com.bootdo.modular.data.domain.VendorDO;

import java.util.List;
import java.util.Map;

/**
 * 供应商信息表
 *
 * @author yogiCai
 * @date 2017-11-24 23:12:54
 */
public interface VendorDao {

    VendorDO get(Integer id);

    List<VendorDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(VendorDO vendor);

    int update(VendorDO vendor);

    int remove(Integer id);

    int batchRemove(Integer[] ids);
}
