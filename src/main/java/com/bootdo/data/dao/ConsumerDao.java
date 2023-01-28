package com.bootdo.data.dao;


import com.bootdo.data.domain.ConsumerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 客户信息表
 * @author yogiCai
 * @date 2017-11-18 22:41:14
 */
@Mapper
public interface ConsumerDao {

	ConsumerDO get(Integer id);
	
	List<ConsumerDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ConsumerDO consumer);
	
	int update(ConsumerDO consumer);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
