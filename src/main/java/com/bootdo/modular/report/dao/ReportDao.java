package com.bootdo.modular.report.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.modular.report.result.SReconItem;
import com.bootdo.modular.report.result.SaleProductItem;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @since 2018-07-07 01:52:07
 */

public interface ReportDao extends MPJBaseMapper<Object> {

    List<SReconItem> sReconCustomer(@Param("param")Map<String, Object> param);

    Page<SReconItem> sReconCustomerItem(Page<SReconItem> page, @Param("param")Map<String, Object> param);

    List<SReconItem> sReconVendor(@Param("param")Map<String, Object> param);

    Page<SReconItem> sReconVendorItem(Page<SReconItem> page, @Param("param")Map<String, Object> param);

    List<SaleProductItem> saleProduct(@Param("param")Map<String, Object> param);

}
