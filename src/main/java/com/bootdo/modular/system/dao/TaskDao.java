package com.bootdo.modular.system.dao;

import com.bootdo.modular.system.domain.TaskDO;

import java.util.List;
import java.util.Map;

/**
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 15:45:42
 */
public interface TaskDao {

    TaskDO get(Long id);

    List<TaskDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(TaskDO task);

    int update(TaskDO task);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
