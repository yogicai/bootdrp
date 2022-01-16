package com.bootdo.se.service;


import com.bootdo.common.utils.NumberUtils;
import com.bootdo.data.dao.ConsumerDao;
import com.bootdo.data.dao.ProductCostDao;
import com.bootdo.data.dao.ProductDao;
import com.bootdo.data.domain.ConsumerDO;
import com.bootdo.data.domain.ProductCostDO;
import com.bootdo.data.domain.ProductDO;
import com.bootdo.data.domain.StockDO;
import com.bootdo.data.service.StockService;
import com.bootdo.se.controller.request.SEOrderEntryVO;
import com.bootdo.se.controller.request.SEOrderVO;
import com.bootdo.se.convert.SEOrderConverter;
import com.bootdo.se.dao.SEOrderDao;
import com.bootdo.se.dao.SEOrderEntryDao;
import com.bootdo.se.domain.SEOrderDO;
import com.bootdo.se.domain.SEOrderEntryDO;
import com.bootdo.system.dao.UserDao;
import com.bootdo.system.domain.UserDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class SEOrderEntryService {
    @Autowired
    private SEOrderDao orderDao;
    @Autowired
    private SEOrderEntryDao orderEntryDao;
    @Autowired
    private ConsumerDao consumerDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductCostDao productCostDao;

    @Transactional
    public SEOrderDO save(SEOrderVO orderVO){
        UserDO userDO = userDao.get(NumberUtils.toLong(orderVO.getBillerId()));
        ConsumerDO consumerDO = consumerDao.get(NumberUtils.toInt(orderVO.getConsumerId()));
        Map<String, StockDO> stockDOMap = stockService.listStock(Maps.newHashMap());
        Map<String, ProductCostDO> costDOMap = convertProductCostMap(orderVO);
        Map<String, BigDecimal> purchaseMap = convertPurchaseMap(orderVO);//商品采购价信息
        SEOrderDO orderDO = SEOrderConverter.convertOrder(orderVO, userDO, consumerDO);
        List<SEOrderEntryDO> orderEntryDOList = SEOrderConverter.convertOrderEntry(orderVO, orderDO, stockDOMap, costDOMap, purchaseMap);
        //订单入库
        orderDao.save(orderDO);
        orderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        orderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public SEOrderVO getOrderVO(Map<String, Object> params) {
        List<SEOrderDO> orderDOList = orderDao.list(params);
        List<SEOrderEntryDO> orderEntryDOList = orderEntryDao.list(params);
        if (CollectionUtils.isEmpty(orderDOList) || CollectionUtils.isEmpty(orderEntryDOList)) {
            return new SEOrderVO();
        }
        SEOrderVO orderVO = new SEOrderVO();
        SEOrderDO orderDO = orderDOList.get(0);
        orderVO.setBillDate(orderDO.getBillDate());
        orderVO.setBillNo(orderDO.getBillNo());
        orderVO.setBillerId(orderDO.getBillerId());
        orderVO.setConsumerId(orderDO.getConsumerId());
        orderVO.setConsumerName(orderDO.getConsumerName());
        orderVO.setEntryAmountTotal(orderDO.getEntryAmount());
        orderVO.setFinalAmountTotal(orderDO.getFinalAmount());
        orderVO.setPaymentAmountTotal(orderDO.getPaymentAmount());
        orderVO.setDiscountAmountTotal(orderDO.getDiscountAmount());
        orderVO.setDiscountRateTotal(orderDO.getDiscountRate());
        orderVO.setSettleAccountTotal(orderDO.getSettleAccount());
        orderVO.setDebtAccountTotal(NumberUtils.subtract(NumberUtils.add(orderDO.getFinalAmount(), orderDO.getExpenseFee()), orderDO.getPaymentAmount()));
        orderVO.setExpenseFeeTotal(orderDO.getExpenseFee());
        orderVO.setPurchaseFeeTotal(orderDO.getPurchaseFee());
        orderVO.setRemark(orderDO.getRemark());
        orderVO.setAuditStatus(orderDO.getAuditStatus());
        for (SEOrderEntryDO orderEntryDO : orderEntryDOList) {
            SEOrderEntryVO entryVO = new SEOrderEntryVO();
            entryVO.setId(orderEntryDO.getId());
            entryVO.setEntryId(orderEntryDO.getEntryId());
            entryVO.setEntryName(orderEntryDO.getEntryName());
            entryVO.setEntryUnit(orderEntryDO.getEntryUnit());
            entryVO.setEntryPrice(orderEntryDO.getEntryPrice());
            entryVO.setStockNo(orderEntryDO.getStockNo());
            entryVO.setStockName(orderEntryDO.getStockName());
            entryVO.setTotalQty(orderEntryDO.getTotalQty());
            entryVO.setEntryAmount(orderEntryDO.getEntryAmount());
            entryVO.setDiscountAmount(orderEntryDO.getDiscountAmount());
            entryVO.setDiscountRate(orderEntryDO.getDiscountRate());
            entryVO.setPurchaseFee(orderEntryDO.getPurchaseFee());
            entryVO.setTotalAmount(orderEntryDO.getTotalAmount());
            entryVO.setRemark(orderEntryDO.getRemark());
            entryVO.setRequestBillNo(orderEntryDO.getRequestBillNo());
            orderVO.getEntryVOList().add(entryVO);
        }
        return orderVO;
    }

    private Map<String, ProductCostDO> convertProductCostMap(SEOrderVO orderVO) {
        List<String> entryNos = Lists.newArrayList();
        Map<String, ProductCostDO> result = Maps.newHashMap();
        List<SEOrderEntryVO> entryVOList = orderVO.getEntryVOList();
        for (SEOrderEntryVO entryVO : entryVOList) {
            entryNos.add(entryVO.getEntryId());
        }
        List<ProductCostDO> productCostDOList = productCostDao.listLate(ImmutableMap.of("productNos", entryNos));
        for (ProductCostDO productCostDO : productCostDOList) {
            result.putIfAbsent(productCostDO.getProductNo(), productCostDO);
        }
        return result;
    }

    private Map<String, BigDecimal> convertPurchaseMap(SEOrderVO orderVO) {
        List<String> entryNos = Lists.newArrayList();
        Map<String, BigDecimal> result = Maps.newHashMap();
        List<SEOrderEntryVO> entryVOList = orderVO.getEntryVOList();
        for (SEOrderEntryVO entryVO : entryVOList) {
            entryNos.add(entryVO.getEntryId());
        }
        List<ProductDO> ProductDOList = productDao.list(ImmutableMap.of("nos", entryNos));
        for (ProductDO productDO : ProductDOList) {
            result.put(productDO.getNo().toString(), productDO.getPurchasePrice());
        }
        return result;
    }

}