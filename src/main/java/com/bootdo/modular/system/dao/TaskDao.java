package com.bootdo.modular.system.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.bootdo.modular.system.domain.TaskDO;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 15:45:42
 */
@InterceptorIgnore
public interface TaskDao extends MPJBaseMapper<TaskDO> {

}
