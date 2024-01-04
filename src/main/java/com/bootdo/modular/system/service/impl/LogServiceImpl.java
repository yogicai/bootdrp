package com.bootdo.modular.system.service.impl;

import com.bootdo.modular.system.dao.LogDao;
import com.bootdo.modular.system.domain.LogDO;
import com.bootdo.modular.system.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Service
public class LogServiceImpl implements LogService {
	@Resource
    private LogDao logDao;

	public List<LogDO> list(Map<String, Object> map){
		return logDao.list(map);
	}

	public int count(Map<String, Object> map){
		return logDao.count(map);
	}

	public int remove(Long id) {
        return logDao.remove(id);
	}

	public int batchRemove(Long[] ids){
		return logDao.batchRemove(ids);
	}
}
