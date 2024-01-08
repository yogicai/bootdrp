package com.bootdo.modular.rp.service;

import com.bootdo.core.enums.BillType;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.dao.ConsumerDao;
import com.bootdo.modular.data.dao.VendorDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.dao.RPOrderEntryDao;
import com.bootdo.modular.rp.dao.RPOrderSettleDao;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.rp.param.RPOrderEntryVO;
import com.bootdo.modular.rp.param.RPOrderSettleVO;
import com.bootdo.modular.rp.param.RPOrderVO;
import com.bootdo.modular.rp.convert.RPOrderConverter;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author L
 */
@Service
public class RPOrderEntryService {
    @Resource
    private RPOrderDao rpOrderDao;
    @Resource
    private RPOrderEntryDao rpOrderEntryDao;
    @Resource
    private RPOrderSettleDao rpOrderSettleDao;
    @Resource
    private ConsumerDao consumerDao;
    @Resource
    private VendorDao vendorDao;
    @Resource
    private UserDao userDao;
    @Resource
    private AccountDao accountDao;

    public RPOrderEntryDO get(Integer id) {
        return rpOrderEntryDao.get(id);
    }

    public List<RPOrderEntryDO> list(Map<String, Object> map) {
        return rpOrderEntryDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return rpOrderEntryDao.count(map);
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
        rpOrderDao.save(orderDO);
        //收款结算明细入库
        rpOrderSettleDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        rpOrderSettleDao.saveBatch(orderSettleDOList);
        //源单核销金额入库
        rpOrderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        rpOrderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public RPOrderVO getOrderVO(Map<String, Object> params) {
        List<RPOrderDO> orderDOList = rpOrderDao.list(params);
        List<RPOrderEntryDO> orderEntryDOList = rpOrderEntryDao.list(params);
        List<RPOrderSettleDO> orderSettleDOList = rpOrderSettleDao.list(params);
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
