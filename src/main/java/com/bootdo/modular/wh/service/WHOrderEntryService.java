package com.bootdo.modular.wh.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.service.ConsumerService;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.service.ProductCostService;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.wh.convert.WHOrderConverter;
import com.bootdo.modular.wh.dao.WHOrderEntryDao;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.bootdo.modular.wh.domain.WHOrderEntryDO;
import com.bootdo.modular.wh.param.WHOrderEntryVO;
import com.bootdo.modular.wh.param.WHOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class WHOrderEntryService extends ServiceImpl<WHOrderEntryDao, WHOrderEntryDO> {
    @Resource
    private WHOrderService whOrderService;
    @Resource
    private ConsumerService consumerService;
    @Resource
    private StockService stockService;
    @Resource
    private ProductCostService productCostService;

    @Transactional(rollbackFor = Exception.class)
    public WHOrderDO save(WHOrderVO orderVO) {
        ConsumerDO consumerDO = consumerService.getByNo(orderVO.getDebtorId());
        Map<String, StockDO> stockMap = stockService.listStock();
        Map<String, ProductCostDO> costMap = convertProductCostMap(orderVO);
        WHOrderDO orderDO = WHOrderConverter.convertOrder(orderVO, consumerDO);
        List<WHOrderEntryDO> orderEntryDOList = WHOrderConverter.convertOrderEntry(orderVO, orderDO, stockMap, costMap);
        //订单入库
        whOrderService.saveOrUpdate(orderDO, Wrappers.lambdaUpdate(WHOrderDO.class).eq(WHOrderDO::getBillNo, orderDO.getBillNo()));
        this.remove(Wrappers.lambdaQuery(WHOrderEntryDO.class).eq(WHOrderEntryDO::getBillNo, orderDO.getBillNo()));
        this.saveBatch(orderEntryDOList);

        return orderDO;
    }

    public WHOrderVO getOrderVO(OrderDetailParam param) {
        WHOrderDO orderDO = whOrderService.getOne(Wrappers.lambdaQuery(WHOrderDO.class).eq(WHOrderDO::getBillNo, param.getBillNo()));
        List<WHOrderEntryDO> orderEntryDOList = this.list(Wrappers.lambdaQuery(WHOrderEntryDO.class).eq(WHOrderEntryDO::getBillNo, param.getBillNo()));
        if (ObjectUtil.isEmpty(orderDO) || ObjectUtil.isEmpty(orderEntryDOList)) {
            return new WHOrderVO();
        }
        WHOrderVO orderVO = new WHOrderVO();
        orderVO.setBillDate(orderDO.getBillDate());
        orderVO.setShopNo(orderDO.getShopNo());
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
        List<String> entryNos = orderVO.getEntryVOList().stream().map(WHOrderEntryVO::getEntryId).collect(Collectors.toList());

        return productCostService.listLate(entryNos).stream()
                .collect(Collectors.toMap(ProductCostDO::getProductNo, Function.identity(), (o, n) -> o));
    }
}
