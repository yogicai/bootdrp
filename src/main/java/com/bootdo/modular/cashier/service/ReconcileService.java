package com.bootdo.modular.cashier.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.cashier.dao.ReconcileDao;
import com.bootdo.modular.cashier.param.ReconcileParam;
import com.bootdo.modular.cashier.result.ReconcileResult.ReconcileItem;
import com.bootdo.modular.rp.domain.RPOrderDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 经营概况
 *
 * @author L
 * @since 2024-01-08 13:35
 */
@Slf4j
@Service
public class ReconcileService extends ServiceImpl<ReconcileDao, RPOrderDO> {
    @Resource
    private ReconcileDao reconcileDao;


    public PageJQ page(ReconcileParam param) {
        Page<ReconcileItem> page = reconcileDao.list(PageFactory.defaultPage(), BeanUtil.beanToMap(param));
        Map<String, Object> map = reconcileDao.selectSum(BeanUtil.beanToMap(param));
        return new PageJQ(page, map);
    }

    public void export(ReconcileParam param) {
        List<ReconcileItem> orderList = reconcileDao.list(BeanUtil.beanToMap(param));
        String pureStart = DateUtil.format(param.getStart(), DatePattern.PURE_DATE_FORMAT);
        String pureEnd = DateUtil.format(param.getEnd(), DatePattern.PURE_DATE_FORMAT);
        String fileName = StrUtil.format("收款对账单_{}-{}.xlsx", pureStart, pureEnd);
        PoiUtil.exportExcelWithStream(fileName, ReconcileItem.class, orderList);
    }
}
