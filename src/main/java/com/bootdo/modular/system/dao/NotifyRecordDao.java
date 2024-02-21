package com.bootdo.modular.system.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * 通知通告发送记录
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-09 17:18:45
 */
@InterceptorIgnore
public interface NotifyRecordDao extends MPJBaseMapper<NotifyRecordDO> {

}
