package com.bootdo.modular.po.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.data.service.VendorService;
import com.bootdo.modular.po.convert.OrderConverter;
import com.bootdo.modular.po.dao.OrderEntryDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.domain.OrderEntryDO;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.po.param.OrderEntryVO;
import com.bootdo.modular.po.param.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Service
public class OrderEntryService extends ServiceImpl<OrderEntryDao, OrderEntryDO> {
    @Resource
    private OrderService orderService;
    @Resource
    private VendorService vendorService;
    @Resource
    private StockService stockService;

    @Transactional(rollbackFor = Exception.class)
    public OrderDO save(OrderVO orderVO) {
        VendorDO vendorDO = vendorService.getByNo(orderVO.getVendorId());
        Map<String, StockDO> stockMap = stockService.listStock();
        OrderDO orderDO = OrderConverter.convertOrder(orderVO, vendorDO);
        List<OrderEntryDO> orderEntryDOList = OrderConverter.convertOrderEntry(orderVO, orderDO, stockMap);
        //订单入库
        orderService.saveOrUpdate(orderDO, Wrappers.lambdaUpdate(OrderDO.class).eq(OrderDO::getBillNo, orderDO.getBillNo()));
        this.remove(Wrappers.lambdaQuery(OrderEntryDO.class).eq(OrderEntryDO::getBillNo, orderDO.getBillNo()));
        this.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public OrderVO getOrderVO(OrderDetailParam param) {
        OrderDO orderDO = orderService.getOne(Wrappers.lambdaQuery(OrderDO.class).eq(OrderDO::getBillNo, param.getBillNo()));
        List<OrderEntryDO> orderEntryDOList = this.list(Wrappers.lambdaQuery(OrderEntryDO.class).eq(OrderEntryDO::getBillNo, param.getBillNo()));
        if (ObjectUtil.isEmpty(orderDO) || ObjectUtil.isEmpty(orderEntryDOList)) {
            return new OrderVO();
        }
        OrderVO orderVO = new OrderVO();
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
