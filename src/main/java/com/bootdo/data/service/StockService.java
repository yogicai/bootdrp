package com.bootdo.data.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.bootdo.data.dao.StockDao;
import com.bootdo.data.domain.StockDO;



@Service
public class StockService {
	@Autowired
	private StockDao stockDao;
	
	public StockDO get(Integer id){
		return stockDao.get(id);
	}
	
	public List<StockDO> list(Map<String, Object> map){
		return stockDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return stockDao.count(map);
	}
	
	public int save(StockDO stock){
		return stockDao.save(stock);
	}
	
	public int update(StockDO stock){
		return stockDao.update(stock);
	}
	
	public int remove(Integer id){
		return stockDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return stockDao.batchRemove(ids);
	}

    public Map<String, StockDO> listStock(Map<String, Object> map) {
	    List<StockDO> list = stockDao.list(map);
	    Map<String, StockDO> stockDOMap = Maps.newHashMap();
	    for (StockDO stockDO : list) {
            stockDOMap.put(stockDO.getStockNo(), stockDO);
        }
        return stockDOMap;
    }
}
