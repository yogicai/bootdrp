package com.bootdo.modular.wh.service;

import com.bootdo.modular.data.dao.ConsumerDao;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.engage.dao.ProductCostDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.wh.dao.WHOrderDao;
import com.bootdo.modular.wh.controller.request.WHOrderEntryVO;
import com.bootdo.modular.wh.controller.request.WHOrderVO;
import com.bootdo.modular.wh.convert.WHOrderConverter;
import com.bootdo.modular.wh.dao.WHOrderEntryDao;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.bootdo.modular.wh.domain.WHOrderEntryDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author L
 */
@Service
public class WHOrderEntryService {
    @Resource
    private WHOrderDao whOrderDao;
    @Resource
    private WHOrderEntryDao whOrderEntryDao;
    @Resource
    private ConsumerDao consumerDao;
    @Resource
    private StockService stockService;
    @Resource
    private ProductCostDao productCostDao;

    @Transactional(rollbackFor = Exception.class)
    public WHOrderDO save(WHOrderVO orderVO) {
        ConsumerDO consumerDO = consumerDao.get(NumberUtils.toInt(orderVO.getDebtorId()));
        Map<String, StockDO> stockDOMap = stockService.listStock(Maps.newHashMap());
        Map<String, ProductCostDO> costDOMap = convertProductCostMap(orderVO);
        WHOrderDO orderDO = WHOrderConverter.convertOrder(orderVO, consumerDO);
        List<WHOrderEntryDO> orderEntryDOList = WHOrderConverter.convertOrderEntry(orderVO, orderDO, stockDOMap, costDOMap);
        //订单入库
        whOrderDao.save(orderDO);
        whOrderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        whOrderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public WHOrderVO getOrderVO(Map<String, Object> params) {
        List<WHOrderDO> orderDOList = whOrderDao.list(params);
        List<WHOrderEntryDO> orderEntryDOList = whOrderEntryDao.list(params);
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
