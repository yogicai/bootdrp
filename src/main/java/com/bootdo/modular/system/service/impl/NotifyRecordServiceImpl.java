package com.bootdo.modular.system.service.impl;

import com.bootdo.modular.system.dao.NotifyRecordDao;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.bootdo.modular.system.service.NotifyRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author L
 */
@Service
public class NotifyRecordServiceImpl implements NotifyRecordService {
    @Resource
    private NotifyRecordDao notifyRecordDao;

    @Override
    public NotifyRecordDO get(Long id) {
        return notifyRecordDao.get(id);
    }

    @Override
    public List<NotifyRecordDO> list(Map<String, Object> map) {
        return notifyRecordDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return notifyRecordDao.count(map);
    }

    @Override
    public int save(NotifyRecordDO notifyRecord) {
        return notifyRecordDao.save(notifyRecord);
    }

    @Override
    public int update(NotifyRecordDO notifyRecord) {
        return notifyRecordDao.update(notifyRecord);
    }

    @Override
    public int remove(Long id) {
        return notifyRecordDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return notifyRecordDao.batchRemove(ids);
    }

    @Override
    public int changeRead(NotifyRecordDO notifyRecord) {
        return notifyRecordDao.changeRead(notifyRecord);
    }

}
