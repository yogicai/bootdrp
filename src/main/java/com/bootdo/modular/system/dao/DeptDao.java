package com.bootdo.modular.system.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.bootdo.modular.system.domain.DeptDO;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * 部门管理
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 15:35:39
 */
@InterceptorIgnore
public interface DeptDao extends MPJBaseMapper<DeptDO> {

}
