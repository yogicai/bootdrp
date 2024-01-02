package com.bootdo.data.service;

import com.bootdo.data.dao.AccountDao;
import com.bootdo.data.domain.AccountDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



/**
 * @author L
 */
@Service
public class AccountService {
	@Resource
	private AccountDao accountDao;
	
	public AccountDO get(Integer id){
		return accountDao.get(id);
	}
	
	public List<AccountDO> list(Map<String, Object> map){
		return accountDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return accountDao.count(map);
	}
	
	public int save(AccountDO account){
		return accountDao.save(account);
	}
	
	public int update(AccountDO account){
		return accountDao.update(account);
	}
	
	public int remove(Integer id){
		return accountDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return accountDao.batchRemove(ids);
	}
	
}
