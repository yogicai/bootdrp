package com.bootdo.modular.se.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.service.ConsumerService;
import com.bootdo.modular.data.service.ProductService;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.service.ProductCostService;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.se.convert.SEOrderConverter;
import com.bootdo.modular.se.dao.SEOrderEntryDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.domain.SEOrderEntryDO;
import com.bootdo.modular.se.param.SEOrderEntryVO;
import com.bootdo.modular.se.param.SEOrderVO;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.domain.UserDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author L
 */
@Service
public class SEOrderEntryService extends ServiceImpl<SEOrderEntryDao, SEOrderEntryDO> {
    @Resource
    private SEOrderService seOrderService;
    @Resource
    private ConsumerService consumerService;
    @Resource
    private UserDao userDao;
    @Resource
    private StockService stockService;
    @Resource
    private ProductService productService;
    @Resource
    private ProductCostService productCostService;

    @Transactional(rollbackFor = Exception.class)
    public SEOrderDO save(SEOrderVO orderVO) {
        UserDO userDO = userDao.selectById(orderVO.getBillerId());
        ConsumerDO consumerDO = consumerService.getByNo(orderVO.getConsumerId());
        Map<String, StockDO> stockMap = stockService.listStock();
        Map<String, ProductCostDO> costMap = convertProductCostMap(orderVO);
        Map<String, BigDecimal> purchaseMap = convertPurchaseMap(orderVO);//商品采购价信息
        SEOrderDO orderDO = SEOrderConverter.convertOrder(orderVO, userDO, consumerDO);
        List<SEOrderEntryDO> orderEntryDOList = SEOrderConverter.convertOrderEntry(orderVO, orderDO, stockMap, costMap, purchaseMap);
        //订单入库
        seOrderService.saveOrUpdate(orderDO, Wrappers.lambdaUpdate(SEOrderDO.class).eq(SEOrderDO::getBillNo, orderDO.getBillNo()));
        this.remove(Wrappers.lambdaQuery(SEOrderEntryDO.class).eq(SEOrderEntryDO::getBillNo, orderDO.getBillNo()));
        this.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public SEOrderVO getOrderVO(OrderDetailParam param) {
        SEOrderDO orderDO = seOrderService.getOne(Wrappers.lambdaQuery(SEOrderDO.class).eq(SEOrderDO::getBillNo, param.getBillNo()));
        List<SEOrderEntryDO> orderEntryDOList = this.list(Wrappers.lambdaQuery(SEOrderEntryDO.class).eq(SEOrderEntryDO::getBillNo, param.getBillNo()));
        if (ObjectUtil.isEmpty(orderDO) || ObjectUtil.isEmpty(orderEntryDOList)) {
            return new SEOrderVO();
        }
        SEOrderVO orderVO = new SEOrderVO();
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
        orderVO.setBillSource(ObjectUtil.defaultIfNull(orderDO.getBillSource(), BillSource.USER));
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
        List<String> entryNos = orderVO.getEntryVOList().stream().map(SEOrderEntryVO::getEntryId).collect(Collectors.toList());

        return productCostService.listLate(entryNos)
                .stream()
                .collect(Collectors.toMap(ProductCostDO::getProductNo, Function.identity(), (o, n) -> o));
    }

    private Map<String, BigDecimal> convertPurchaseMap(SEOrderVO orderVO) {
        List<String> entryNos = orderVO.getEntryVOList().stream().map(SEOrderEntryVO::getEntryId).collect(Collectors.toList());

        return productService.list(Wrappers.lambdaQuery(ProductDO.class).in(ProductDO::getNo, entryNos))
                .stream()
                .collect(Collectors.toMap(p -> p.getNo().toString(), ProductDO::getPurchasePrice, (o, n) -> n));
    }

}