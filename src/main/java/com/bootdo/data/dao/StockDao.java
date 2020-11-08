package com.bootdo.data.dao;

import com.bootdo.data.domain.StockDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 仓库表
 * @author yogiCai
 * @date 2018-02-18 16:23:32
 */
@Mapper
public interface StockDao {

	StockDO get(Integer id);
	
	List<StockDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockDO stock);
	
	int update(StockDO stock);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
