package com.bootdo.modular.system.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.bootdo.modular.system.domain.ContentDO;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * 文章内容
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 16:17:48
 */
@InterceptorIgnore
public interface BlogContentDao extends MPJBaseMapper<ContentDO> {

}
