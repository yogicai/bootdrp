package com.bootdo.data.dao;

import com.bootdo.data.domain.VendorDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 供应商信息表
 * @Author: yogiCai
 * @date 2017-11-24 23:12:54
 */
@Mapper
public interface VendorDao {

	VendorDO get(Integer id);
	
	List<VendorDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(VendorDO vendor);
	
	int update(VendorDO vendor);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
