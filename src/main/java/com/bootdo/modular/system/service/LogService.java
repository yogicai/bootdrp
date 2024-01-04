package com.bootdo.modular.system.service;

import com.bootdo.modular.system.domain.LogDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Service
public interface LogService {
	List<LogDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int remove(Long id);

	int batchRemove(Long[] ids);
}
