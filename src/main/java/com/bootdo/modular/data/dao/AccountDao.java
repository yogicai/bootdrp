package com.bootdo.modular.data.dao;

import com.bootdo.modular.data.domain.AccountDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
public interface AccountDao {

	AccountDO get(Integer id);
	
	List<AccountDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AccountDO account);
	
	int update(AccountDO account);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
