package com.bootdo.modular.report.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.core.enums.InstituteType;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.utils.CollectionUtils;
import com.bootdo.modular.report.dao.ReportDao;
import com.bootdo.modular.report.param.SReconEntryParam;
import com.bootdo.modular.report.param.SReconParam;
import com.bootdo.modular.report.param.SaleProductParam;
import com.bootdo.modular.report.result.SReconItem;
import com.bootdo.modular.report.result.SReconResult;
import com.bootdo.modular.report.result.SaleProductItem;
import com.bootdo.modular.report.result.SaleProductResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author L
 * @since 2020-11-10 14:48
 */
@Service
public class ReportService {
    @Resource
    private ReportDao reportDao;


    /**
     * 客户应收欠款
     * 供应商应付款
     */
    @Transactional(readOnly = true)
    public SReconResult sRecon(SReconParam param) {
        List<SReconItem> itemList;
        if (InstituteType.CUSTOMER.equals(param.getType())) {
            itemList = reportDao.sReconCustomer(BeanUtil.beanToMap(param));
        } else {
            itemList = reportDao.sReconVendor(BeanUtil.beanToMap(param));
        }
        return new SReconResult()
                .setBillRegion(DateUtil.formatDate(param.getStart()) + "_" + DateUtil.formatDate(param.getEnd()))
                .setItemList(itemList);
    }

    @Transactional(readOnly = true)
    public Page<SReconItem> sReconEntryPage(SReconEntryParam param) {
        if (InstituteType.CUSTOMER.equals(param.getType())) {
            return reportDao.sReconCustomerItem(PageFactory.defaultPage(), BeanUtil.beanToMap(param));
        } else {
            return reportDao.sReconVendorItem(PageFactory.defaultPage(), BeanUtil.beanToMap(param));
        }
    }

    /**
     * 销售统计报表
     */
    @Transactional(readOnly = true)
    public SaleProductResult saleProduct(SaleProductParam param) {
        List<SaleProductItem> itemList = reportDao.saleProduct(BeanUtil.beanToMap(param));
        //单据日期范围
        Date startDate = CollectionUtils.getMinValue(itemList, SaleProductItem::getStartDate);
        Date endDate = CollectionUtils.getMaxValue(itemList, SaleProductItem::getEndDate);

        return new SaleProductResult()
                .setBillRegion(DateUtil.formatDate(startDate) + "_" + DateUtil.formatDate(endDate))
                .setItemList(itemList);
    }
}
