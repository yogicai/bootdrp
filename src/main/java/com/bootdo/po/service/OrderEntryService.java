package com.bootdo.po.service;


import com.bootdo.common.enumeration.BillSource;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.data.dao.AccountDao;
import com.bootdo.data.dao.VendorDao;
import com.bootdo.data.domain.AccountDO;
import com.bootdo.data.domain.StockDO;
import com.bootdo.data.domain.VendorDO;
import com.bootdo.data.service.StockService;
import com.bootdo.po.controller.request.OrderEntryVO;
import com.bootdo.po.controller.request.OrderVO;
import com.bootdo.po.convert.OrderConverter;
import com.bootdo.po.dao.OrderDao;
import com.bootdo.po.dao.OrderEntryDao;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.po.domain.OrderEntryDO;
import com.bootdo.rp.convert.RPOrderConverter;
import com.bootdo.rp.dao.RPOrderDao;
import com.bootdo.rp.dao.RPOrderEntryDao;
import com.bootdo.rp.dao.RPOrderSettleDao;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.rp.domain.RPOrderSettleDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class OrderEntryService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderEntryDao orderEntryDao;
    @Autowired
    private VendorDao vendorDao;
    @Autowired
    private StockService stockService;

    @Transactional(rollbackFor = Exception.class)
    public OrderDO save(OrderVO orderVO) {
        VendorDO vendorDO = vendorDao.get(NumberUtils.toInt(orderVO.getVendorId()));
        Map<String, StockDO> stockDOMap = stockService.listStock(Maps.newHashMap());
        OrderDO orderDO = OrderConverter.convertOrder(orderVO, vendorDO);
        List<OrderEntryDO> orderEntryDOList = OrderConverter.convertOrderEntry(orderVO, orderDO, stockDOMap);
        //订单入库
        orderDao.save(orderDO);
        orderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        orderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public OrderVO getOrderVO(Map<String, Object> params) {
        List<OrderDO> orderDOList = orderDao.list(params);
        List<OrderEntryDO> orderEntryDOList = orderEntryDao.list(params);
        if (CollectionUtils.isEmpty(orderDOList) || CollectionUtils.isEmpty(orderEntryDOList)) {
            return new OrderVO();
        }
        OrderVO orderVO = new OrderVO();
        OrderDO orderDO = orderDOList.get(0);
        orderVO.setBillDate(orderDO.getBillDate());
        orderVO.setBillNo(orderDO.getBillNo());
        orderVO.setBillType(orderDO.getBillType());
        orderVO.setVendorId(orderDO.getVendorId());
        orderVO.setVendorName(orderDO.getVendorName());
        orderVO.setEntryAmountTotal(orderDO.getEntryAmount());
        orderVO.setFinalAmountTotal(orderDO.getFinalAmount());
        orderVO.setPaymentAmountTotal(orderDO.getPaymentAmount());
        orderVO.setDiscountAmountTotal(orderDO.getDiscountAmount());
        orderVO.setDiscountRateTotal(orderDO.getDiscountRate());
        orderVO.setSettleAccountTotal(orderDO.getSettleAccount());
        orderVO.setDebtAccountTotal(NumberUtils.subtract(NumberUtils.add(orderDO.getFinalAmount(), orderDO.getPurchaseFee()), orderDO.getPaymentAmount()));
        orderVO.setPurchaseFeeTotal(orderDO.getPurchaseFee());
        orderVO.setRemark(orderDO.getRemark());
        orderVO.setAuditStatus(orderDO.getAuditStatus());
        for (OrderEntryDO orderEntryDO : orderEntryDOList) {
            OrderEntryVO entryVO = new OrderEntryVO();
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
}
