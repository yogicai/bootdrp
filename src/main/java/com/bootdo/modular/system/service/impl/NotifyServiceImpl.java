package com.bootdo.modular.system.service.impl;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.modular.system.dao.NotifyDao;
import com.bootdo.modular.system.dao.NotifyRecordDao;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.domain.NotifyDO;
import com.bootdo.modular.system.domain.NotifyDTO;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.bootdo.modular.system.service.NotifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Service
public class NotifyServiceImpl implements NotifyService {
    @Resource
    private NotifyDao notifyDao;
    @Resource
    private NotifyRecordDao notifyRecordDao;
    @Resource
    private UserDao userDao;

    @Override
    public NotifyDO get(Long id) {
        return notifyDao.get(id);
    }

    @Override
    public List<NotifyDO> list(Map<String, Object> map) {
        return notifyDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return notifyDao.count(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(NotifyDO notify) {
        notify.setUpdateDate(new Date());
        int r = notifyDao.save(notify);
        // 保存到接受者列表中
        Long[] userIds = notify.getUserIds();
        Long notifyId = notify.getId();
        List<NotifyRecordDO> records = new ArrayList<>();
        for (Long userId : userIds) {
            NotifyRecordDO record = new NotifyRecordDO();
            record.setNotifyId(notifyId);
            record.setUserId(userId);
            record.setIsRead(0);
            records.add(record);
        }
        notifyRecordDao.batchSave(records);
        return r;
    }

    @Override
    public int update(NotifyDO notify) {
        return notifyDao.update(notify);
    }

    @Transactional
    @Override
    public int remove(Long id) {
        notifyRecordDao.removeByNotifbyId(id);
        return notifyDao.remove(id);
    }

    @Transactional
    @Override
    public int batchRemove(Long[] ids) {
        notifyRecordDao.batchRemoveByNotifbyId(ids);
        return notifyDao.batchRemove(ids);
    }

    @Override
    public PageR selfList(Map<String, Object> map) {
        List<NotifyDTO> rows = notifyDao.listDTO(map);
        for (NotifyDTO notifyDTO : rows) {
            notifyDTO.setBefore(DateUtils.getTimeBefore(notifyDTO.getUpdateDate()));
            notifyDTO.setSender(userDao.get(notifyDTO.getCreateBy()).getName());
        }
        PageR page = new PageR(rows, notifyDao.countDTO(map));
        return page;
    }

}
