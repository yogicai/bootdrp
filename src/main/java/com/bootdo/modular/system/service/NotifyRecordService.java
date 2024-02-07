package com.bootdo.modular.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.system.dao.NotifyRecordDao;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author L
 */
@Service
public class NotifyRecordService extends ServiceImpl<NotifyRecordDao, NotifyRecordDO> {

    @Transactional(rollbackFor = Exception.class)
    public boolean changeRead(NotifyRecordDO notifyRecord) {
        Wrapper<NotifyRecordDO> updateEntityWrapper = Wrappers.lambdaUpdate(NotifyRecordDO.class)
                .set(NotifyRecordDO::getReadDate, notifyRecord.getReadDate())
                .set(NotifyRecordDO::getIsRead, notifyRecord.getIsRead())
                .eq(NotifyRecordDO::getNotifyId, notifyRecord.getNotifyId())
                .eq(NotifyRecordDO::getUserId, notifyRecord.getUserId());

        return this.update(updateEntityWrapper);
    }

}
