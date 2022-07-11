package com.bootdo.rp.service;

import com.bootdo.common.enumeration.BillType;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.data.dao.AccountDao;
import com.bootdo.data.dao.ConsumerDao;
import com.bootdo.data.dao.VendorDao;
import com.bootdo.data.domain.AccountDO;
import com.bootdo.data.domain.ConsumerDO;
import com.bootdo.data.domain.VendorDO;
import com.bootdo.rp.controller.request.RPOrderEntryVO;
import com.bootdo.rp.controller.request.RPOrderSettleVO;
import com.bootdo.rp.controller.request.RPOrderVO;
import com.bootdo.rp.convert.RPOrderConverter;
import com.bootdo.rp.dao.RPOrderDao;
import com.bootdo.rp.dao.RPOrderEntryDao;
import com.bootdo.rp.dao.RPOrderSettleDao;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.rp.domain.RPOrderSettleDO;
import com.bootdo.system.dao.UserDao;
import com.bootdo.system.domain.UserDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class RPOrderEntryService {
    @Autowired
    private RPOrderDao orderDao;
    @Autowired
    private RPOrderEntryDao orderEntryDao;
    @Autowired
    private RPOrderSettleDao orderSettleDao;
    @Autowired
    private ConsumerDao consumerDao;
    @Autowired
    private VendorDao vendorDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

    public RPOrderEntryDO get(Integer id) {
        return orderEntryDao.get(id);
    }

    public List<RPOrderEntryDO> list(Map<String, Object> map) {
        return orderEntryDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return orderEntryDao.count(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public RPOrderDO save(RPOrderVO orderVO){
        Set<String> settleSet = Sets.newHashSet();
        for (RPOrderSettleVO orderSettleVO : orderVO.getSettleVOList()) {
            settleSet.add(orderSettleVO.getSettleAccount());
        }
        //收款单取客户信息，付款单取供应商信息
        String detectName = "";
        if (BillType.CW_SK_ORDER.equals(orderVO.getBillType())) {
            ConsumerDO consumerDO = consumerDao.get(NumberUtils.toInt(orderVO.getDebtorId()));
            detectName = consumerDO.getName();
        } else {
            VendorDO vendorDO = vendorDao.get(NumberUtils.toInt(orderVO.getDebtorId()));
            detectName = vendorDO.getName();
        }

        UserDO userDO = userDao.get(NumberUtils.toLong(orderVO.getCheckId()));
        List<AccountDO> accountDOList = accountDao.list(ImmutableMap.of("nos", settleSet));
        RPOrderDO orderDO = RPOrderConverter.convertOrder(orderVO, userDO, detectName);
        List<RPOrderEntryDO> orderEntryDOList = RPOrderConverter.convertOrderEntry(orderVO, orderDO);
        List<RPOrderSettleDO> orderSettleDOList = RPOrderConverter.convertOrderSettle(orderVO, orderDO, RPOrderConverter.convertAccountMap(accountDOList));

        //订单入库
        orderDao.save(orderDO);
        //收款结算明细入库
        orderSettleDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        orderSettleDao.saveBatch(orderSettleDOList);
        //源单核销金额入库
        orderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        orderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public RPOrderVO getOrderVO(Map<String, Object> params) {
        List<RPOrderDO> orderDOList = orderDao.list(params);
        List<RPOrderEntryDO> orderEntryDOList = orderEntryDao.list(params);
        List<RPOrderSettleDO> orderSettleDOList = orderSettleDao.list(params);
        if (CollectionUtils.isEmpty(orderDOList)) {
            return new RPOrderVO();
        }
        RPOrderVO orderVO = new RPOrderVO();
        RPOrderDO orderDO = orderDOList.get(0);
        orderVO.setBillDate(orderDO.getBillDate());
        orderVO.setBillNo(orderDO.getBillNo());
        orderVO.setBillType(orderDO.getBillType());
        orderVO.setDebtorId(orderDO.getDebtorId());
        orderVO.setDebtorName(orderDO.getDebtorName());
        orderVO.setCheckId(orderDO.getCheckId());
        orderVO.setDebtorName(orderDO.getCheckName());
        orderVO.setDiscountAmount(orderDO.getDiscountAmount());
        orderVO.setRemark(orderDO.getRemark());
        orderVO.setAuditStatus(orderDO.getAuditStatus());
        for (RPOrderEntryDO orderEntryDO : orderEntryDOList) {
            RPOrderEntryVO entryVO = new RPOrderEntryVO();
            entryVO.setId(orderEntryDO.getId());
            entryVO.setSrcBillNo(orderEntryDO.getSrcBillNo());
            entryVO.setSrcBillType(orderEntryDO.getSrcBillType());
            entryVO.setSrcBillDate(orderEntryDO.getSrcBillDate());
            entryVO.setSrcTotalAmount(orderEntryDO.getSrcTotalAmount());
            entryVO.setSrcPaymentAmount(orderEntryDO.getSrcPaymentAmount());
            entryVO.setCheckAmount(orderEntryDO.getCheckAmount());
            orderVO.getEntryVOList().add(entryVO);
        }
        for (RPOrderSettleDO orderSettleDO : orderSettleDOList) {
            RPOrderSettleVO entryVO = new RPOrderSettleVO();
            entryVO.setId(orderSettleDO.getId());
            entryVO.setSettleAccount(orderSettleDO.getSettleAccount());
            entryVO.setPaymentAmount(orderSettleDO.getPaymentAmount());
            entryVO.setRemark(orderSettleDO.getRemark());
            orderVO.getSettleVOList().add(entryVO);
        }
        return orderVO;
    }

}
