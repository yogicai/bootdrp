package com.bootdo.modular.system.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.bootdo.modular.system.domain.UserDO;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 09:45:11
 */
@InterceptorIgnore
public interface UserDao extends MPJBaseMapper<UserDO> {

}
