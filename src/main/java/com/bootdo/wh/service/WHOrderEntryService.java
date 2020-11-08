package com.bootdo.wh.service;

import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.BillType;
import com.bootdo.common.utils.OrderUtils;
import com.bootdo.common.utils.ShiroUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.data.dao.ProductCostDao;
import com.bootdo.data.dao.VendorDao;
import com.bootdo.data.domain.ProductCostDO;
import com.bootdo.data.domain.StockDO;
import com.bootdo.data.domain.VendorDO;
import com.bootdo.data.service.StockService;
import com.bootdo.wh.controller.request.WHOrderEntryVO;
import com.bootdo.wh.controller.request.WHOrderVO;
import com.bootdo.wh.convert.WHOrderConverter;
import com.bootdo.wh.dao.WHOrderDao;
import com.bootdo.wh.dao.WHOrderEntryDao;
import com.bootdo.wh.domain.WHOrderDO;
import com.bootdo.wh.domain.WHOrderEntryDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class WHOrderEntryService {
	@Autowired
	private WHOrderDao orderDao;
    @Autowired
    private WHOrderEntryDao orderEntryDao;
    @Autowired
    private VendorDao vendorDao;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductCostDao productCostDao;

    @Transactional
    public WHOrderDO save(WHOrderVO orderVO){
        VendorDO vendorDO = vendorDao.get(NumberUtils.toInt(orderVO.getDebtorId()));
        Map<String, StockDO> stockDOMap = stockService.listStock(Maps.newHashMap());
        Map<String, ProductCostDO> costDOMap = convertProductCostMap(orderVO);
        WHOrderDO orderDO = WHOrderConverter.convertOrder(orderVO, vendorDO);
        List<WHOrderEntryDO> orderEntryDOList = WHOrderConverter.convertOrderEntry(orderVO, orderDO, stockDOMap, costDOMap);
        //订单入库
        orderDao.save(orderDO);
        orderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        orderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public WHOrderVO getOrderVO(Map<String, Object> params) {
        List<WHOrderDO> orderDOList = orderDao.list(params);
        List<WHOrderEntryDO> orderEntryDOList = orderEntryDao.list(params);
        if (CollectionUtils.isEmpty(orderDOList) || CollectionUtils.isEmpty(orderEntryDOList)) {
            return new WHOrderVO();
        }
        WHOrderVO orderVO = new WHOrderVO();
        WHOrderDO orderDO = orderDOList.get(0);
        orderVO.setBillDate(orderDO.getBillDate());
        orderVO.setBillNo(orderDO.getBillNo());
        orderVO.setBillType(orderDO.getBillType());
        orderVO.setServiceType(orderDO.getServiceType());
        orderVO.setDebtorId(orderDO.getDebtorId());
        orderVO.setDebtorName(orderDO.getDebtorName());
        orderVO.setTotalQty(orderDO.getTotalQty());
        orderVO.setEntryAmount(orderDO.getEntryAmount());
        orderVO.setAuditId(orderDO.getAuditId());
        orderVO.setAuditName(orderDO.getAuditName());
        orderVO.setAuditStatus(orderDO.getAuditStatus());
        orderVO.setRemark(orderDO.getRemark());
        for (WHOrderEntryDO orderEntryDO : orderEntryDOList) {
            WHOrderEntryVO entryVO = new WHOrderEntryVO();
            entryVO.setId(orderEntryDO.getId());
            entryVO.setEntryId(orderEntryDO.getEntryId());
            entryVO.setEntryName(orderEntryDO.getEntryName());
            entryVO.setEntryUnit(orderEntryDO.getEntryUnit());
            entryVO.setEntryPrice(orderEntryDO.getEntryPrice());
            entryVO.setStockNo(orderEntryDO.getStockNo());
            entryVO.setStockName(orderEntryDO.getStockName());
            entryVO.setTotalQty(orderEntryDO.getTotalQty());
            entryVO.setEntryAmount(orderEntryDO.getEntryAmount());
            orderVO.getEntryVOList().add(entryVO);
        }
        return orderVO;
    }

    private Map<String, ProductCostDO> convertProductCostMap(WHOrderVO orderVO) {
        List<String> entryNos = Lists.newArrayList();
        Map<String, ProductCostDO> result = Maps.newHashMap();
        List<WHOrderEntryVO> entryVOList = orderVO.getEntryVOList();
        for (WHOrderEntryVO entryVO : entryVOList) {
            entryNos.add(entryVO.getEntryId());
        }
        List<ProductCostDO> productCostDOList = productCostDao.listLate(ImmutableMap.of("productNos", entryNos));
        for (ProductCostDO productCostDO : productCostDOList) {
            result.put(productCostDO.getProductNo().toString(), productCostDO);
        }
        return result;
    }
}
