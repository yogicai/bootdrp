package com.bootdo.modular.system.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.modular.system.dao.NotifyDao;
import com.bootdo.modular.system.dao.NotifyRecordDao;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.domain.NotifyDO;
import com.bootdo.modular.system.domain.NotifyDTO;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.bootdo.modular.system.param.SysNotifyParam;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author L
 */
@Service
public class NotifyService extends ServiceImpl<NotifyDao, NotifyDO> {
    @Resource
    private NotifyRecordDao notifyRecordDao;
    @Resource
    private UserDao userDao;


    public PageR page(SysNotifyParam param) {
        return new PageR(this.baseMapper.selectJoinPage(PageFactory.defaultPage(), NotifyDO.class, selectJoinWrapper(param)));
    }

    public List<NotifyDO> list(SysNotifyParam param) {
        return this.baseMapper.selectJoinPage(PageFactory.defalultAllPage(), NotifyDO.class, selectJoinWrapper(param)).getRecords();
    }

    public MPJLambdaWrapper<NotifyDO> selectJoinWrapper(SysNotifyParam param) {
        return JoinWrappers.lambda(NotifyDO.class)
                .leftJoin(NotifyRecordDO.class, NotifyRecordDO::getNotifyId, NotifyDO::getId)
                .eq(ObjectUtil.isNotEmpty(param.getUserId()), NotifyRecordDO::getUserId, param.getUserId())
                .eq(ObjectUtil.isNotEmpty(param.getIsRead()), NotifyRecordDO::getIsRead, param.getIsRead())
                .ge(ObjectUtil.isNotEmpty(param.getStart()), NotifyDO::getCreateDate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), NotifyDO::getCreateDate, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(NotifyDO::getTitle, param.getSearchText()))
                .orderByDesc(NotifyDO::getCreateDate);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveNotify(NotifyDO notify) {
        notify.setUpdateDate(new Date());
        this.save(notify);
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
        records.forEach(record -> notifyRecordDao.insert(record));
    }

    @Transactional
    public void removeNotify(Long id) {
        notifyRecordDao.delete(Wrappers.lambdaQuery(NotifyRecordDO.class).eq(NotifyRecordDO::getNotifyId, id));
        this.baseMapper.deleteById(id);
    }

    @Transactional
    public void batchRemoveNotify(List<Long> ids) {
        notifyRecordDao.delete(Wrappers.lambdaQuery(NotifyRecordDO.class).in(NotifyRecordDO::getNotifyId, ids));
        this.baseMapper.deleteBatchIds(ids);
    }

    public PageR selfList(SysNotifyParam param) {
        Page<NotifyDTO> page = this.baseMapper.selectJoinPage(PageFactory.defaultPage(), NotifyDTO.class, selectJoinWrapper(param)
                .selectAll(NotifyDO.class).select(NotifyRecordDO::getIsRead));

        page.getRecords().forEach(notifyDTO -> {
            notifyDTO.setBefore(DateUtils.getTimeBefore(notifyDTO.getUpdateDate()));
            notifyDTO.setSender(userDao.selectById(notifyDTO.getCreateBy()).getName());
        });
        return new PageR(page);
    }

}
