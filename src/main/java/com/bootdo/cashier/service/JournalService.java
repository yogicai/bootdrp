package com.bootdo.cashier.service;

import com.bootdo.cashier.dao.JournalDao;
import com.bootdo.cashier.domain.JournalDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class JournalService {
    @Autowired
	private JournalDao journalDao;
	
	public JournalDO get(Integer id){
		return journalDao.get(id);
	}
	
	public List<JournalDO> list(Map<String, Object> map){
		return journalDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return journalDao.count(map);
	}
	
	public int save(JournalDO journal){
		return journalDao.save(journal);
	}
	
	public int update(JournalDO journal){
		return journalDao.update(journal);
	}
	
	public int remove(Integer id){
		return journalDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return journalDao.batchRemove(ids);
	}
	
}
