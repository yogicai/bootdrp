package com.bootdo.data.service;

import com.bootdo.data.dao.VendorDao;
import com.bootdo.data.domain.VendorDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author L
 */
@Service
public class VendorService {
	@Resource
	private VendorDao vendorDao;
	
	public VendorDO get(Integer id){
		return vendorDao.get(id);
	}
	
	public List<VendorDO> list(Map<String, Object> map){
		return vendorDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return vendorDao.count(map);
	}
	
	public int save(VendorDO vendor){
		return vendorDao.save(vendor);
	}
	
	public int update(VendorDO vendor){
		return vendorDao.update(vendor);
	}
	
	public int remove(Integer id){
		return vendorDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return vendorDao.batchRemove(ids);
	}
	
}
