package com.bootdo.modular.wh.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.modular.data.service.CostAmountCalculator;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.wh.dao.WHOrderDao;
import com.bootdo.modular.wh.dao.WHOrderEntryDao;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.bootdo.modular.wh.domain.WHOrderEntryDO;
import com.bootdo.modular.wh.param.WHOrderQryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class WHOrderService extends ServiceImpl<WHOrderDao, WHOrderDO> {
    @Resource
    private WHOrderDao whOrderDao;
    @Resource
    private WHOrderEntryDao whOrderEntryDao;
    @Resource
    private CostAmountCalculator costAmountCalculator;


    public PageJQ page(WHOrderQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<WHOrderDO> pageList(Page<WHOrderDO> page, WHOrderQryParam param) {
        LambdaQueryWrapper<WHOrderDO> queryWrapper = Wrappers.lambdaQuery(WHOrderDO.class)
                .in(ObjectUtil.isNotEmpty(param.getServiceType()), WHOrderDO::getServiceType, StrUtil.split(param.getServiceType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getBillType()), WHOrderDO::getBillType, StrUtil.split(param.getBillType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getAuditStatus()), WHOrderDO::getAuditStatus, StrUtil.split(param.getAuditStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), WHOrderDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), WHOrderDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(WHOrderDO::getBillNo, param.getSearchText()).or().like(WHOrderDO::getDebtorName, param.getSearchText()).or().like(WHOrderDO::getRemark, param.getSearchText()))
                .orderByDesc(WHOrderDO::getBillDate).orderByDesc(WHOrderDO::getUpdateTime);

        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(OrderAuditParam param) {
        AuditStatus auditStatus = param.getAuditStatus();
        List<WHOrderDO> orderDOList = this.list(Wrappers.lambdaQuery(WHOrderDO.class).in(WHOrderDO::getBillNo, param.getBillNos()));
        //去除已经是审核（未审核）状态的订单
        List<WHOrderDO> auditOrderList = orderDOList.stream()
                .filter(orderDO -> !auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //审核采购单重新计算商品成本
        auditOrderList.forEach(orderDO -> {
            costAmountCalculator.calcWHBillCost(orderDO, auditStatus);
            this.update(Wrappers.lambdaUpdate(WHOrderDO.class)
                    .set(WHOrderDO::getAuditStatus, auditStatus)
                    .eq(WHOrderDO::getBillNo, orderDO.getBillNo())
                    .ne(WHOrderDO::getAuditStatus, auditStatus));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<String> billNos) {
        whOrderDao.delete(Wrappers.lambdaQuery(WHOrderDO.class).in(WHOrderDO::getBillNo, billNos));
        whOrderEntryDao.delete(Wrappers.lambdaQuery(WHOrderEntryDO.class).in(WHOrderEntryDO::getBillNo, billNos));
    }

}
