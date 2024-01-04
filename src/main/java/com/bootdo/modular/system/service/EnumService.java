package com.bootdo.modular.system.service;

import com.bootdo.modular.system.domain.DictDO;

import java.util.List;
import java.util.Map;

/**
 * @author yogicai
 * @date 2018-01-20 18:28:07
 */
public interface EnumService {

	Map<String, List<DictDO>> listEnum(Map<String, Object> map);
}
