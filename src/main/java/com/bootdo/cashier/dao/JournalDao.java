package com.bootdo.cashier.dao;

import com.bootdo.cashier.domain.JournalDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 日记账
 * @author yogiCai
 * @date 2018-07-14 22:31:58
 */
@Mapper
public interface JournalDao {

	JournalDO get(Integer id);
	
	List<JournalDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(JournalDO journal);
	
	int update(JournalDO journal);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
