package com.bootdo.modular.po.service;

import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.dao.VendorDao;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.po.controller.request.OrderEntryVO;
import com.bootdo.modular.po.controller.request.OrderVO;
import com.bootdo.modular.po.convert.OrderConverter;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.dao.OrderEntryDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.domain.OrderEntryDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Service
public class OrderEntryService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private VendorDao vendorDao;
    @Resource
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
