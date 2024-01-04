package com.bootdo.modular.rp.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.modular.data.dao.ConsumerDao;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.rp.dao.PointEntryDao;
import com.bootdo.modular.rp.domain.PointEntryDO;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author L
 */
@Service
public class RPPointService {
	@Resource
	private PointEntryDao pointEntryDao;
    @Resource
    private ConsumerDao consumerDao;
	
	public PointEntryDO get(Integer id){
		return pointEntryDao.get(id);
	}
	
	public List<PointEntryDO> list(Map<String, Object> map){
	    String type = MapUtil.getStr(map, "type");
        if ("COLLECT".equals(type)) {
            return pointEntryDao.listG(map);
        }
        return pointEntryDao.list(map);
	}
	
	public int count(Map<String, Object> map){
        String type = MapUtil.getStr(map, "type");
        if ("COLLECT".equals(type)) {
            return pointEntryDao.countG(map);
        }
		return pointEntryDao.count(map);
	}
	
	public int save(PointEntryDO pointEntry){
        ConsumerDO consumerDO = consumerDao.get(NumberUtils.toInt(pointEntry.getConsumerId()));
        pointEntry.setConsumerName(consumerDO.getName());
		return pointEntryDao.save(pointEntry);
	}
	
	public int update(PointEntryDO pointEntry){
        ConsumerDO consumerDO = consumerDao.get(NumberUtils.toInt(pointEntry.getConsumerId()));
        pointEntry.setConsumerName(consumerDO.getName());
		return pointEntryDao.update(pointEntry);
	}
	
	public int remove(Integer id){
		return pointEntryDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return pointEntryDao.batchRemove(ids);
	}
	
}
