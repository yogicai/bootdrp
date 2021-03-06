package com.bootdo.data.service;

import com.bootdo.data.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.bootdo.data.dao.ConsumerDao;
import com.bootdo.data.domain.ConsumerDO;



@Service
public class ConsumerService {
	@Autowired
	private ConsumerDao consumerDao;
	
	public ConsumerDO get(Integer id){
		return consumerDao.get(id);
	}
	
	public List<ConsumerDO> list(Map<String, Object> map){
		return consumerDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return consumerDao.count(map);
	}
	
	public int save(ConsumerDO consumer){
		return consumerDao.save(consumer);
	}
	
	public int update(ConsumerDO consumer){
		return consumerDao.update(consumer);
	}
	
	public int remove(Integer id){
		return consumerDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return consumerDao.batchRemove(ids);
	}
	
}
