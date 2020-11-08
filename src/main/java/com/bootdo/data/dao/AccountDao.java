package com.bootdo.data.dao;

import com.bootdo.data.domain.AccountDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: yogiCai
 * @date 2018-02-16 16:30:26
 */
@Mapper
public interface AccountDao {

	AccountDO get(Integer id);
	
	List<AccountDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AccountDO account);
	
	int update(AccountDO account);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
