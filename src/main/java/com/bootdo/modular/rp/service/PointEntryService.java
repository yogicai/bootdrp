package com.bootdo.modular.rp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.service.ConsumerService;
import com.bootdo.modular.rp.dao.PointEntryDao;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.enums.PointSearchType;
import com.bootdo.modular.rp.param.PointQryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author L
 */
@Service
public class PointEntryService extends ServiceImpl<PointEntryDao, PointEntryDO> {
    @Resource
    private ConsumerService consumerService;

    public PageJQ page(PointQryParam param) {
        if (PointSearchType.COLLECT.equals(param.getType())) {
            return new PageJQ(this.pageListG(PageFactory.defaultPage(), param));
        } else {
            return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
        }
    }

    public Page<PointEntryDO> pageList(Page<PointEntryDO> page, PointQryParam param) {
        LambdaQueryWrapper<PointEntryDO> queryWrapper = Wrappers.lambdaQuery(PointEntryDO.class)
                .ge(PointSearchType.INCOME.equals(param.getType()), PointEntryDO::getTotalAmount, BigDecimal.ZERO)
                .lt(PointSearchType.OUTCOME.equals(param.getType()), PointEntryDO::getTotalAmount, BigDecimal.ZERO)
                .in(ObjectUtil.isNotEmpty(param.getConsumerId()), PointEntryDO::getConsumerId, StrUtil.split(param.getConsumerId(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), PointEntryDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), PointEntryDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), PointEntryDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(PointEntryDO::getConsumerName, param.getSearchText()).or().like(PointEntryDO::getRemark, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public Page<PointEntryDO> pageListG(Page<PointEntryDO> page, PointQryParam param) {
        return this.baseMapper.listG(page, BeanUtil.beanToMap(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(PointEntryDO pointEntry) {
        ConsumerDO consumerDO = consumerService.getByNo(pointEntry.getConsumerId());
        pointEntry.setConsumerName(consumerDO.getName());
        this.saveOrUpdate(pointEntry);
    }
}
