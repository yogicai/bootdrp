package com.bootdo.modular.system.dao;

import com.bootdo.modular.system.domain.LogDO;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 15:45:42
 */
public interface LogDao {

    LogDO get(Long id);

    List<LogDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(LogDO log);

    int update(LogDO log);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
