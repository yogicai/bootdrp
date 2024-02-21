package com.bootdo.modular.system.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.bootdo.modular.system.domain.FileDO;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 15:45:42
 */
@InterceptorIgnore
public interface FileDao extends MPJBaseMapper<FileDO> {

}
