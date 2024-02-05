package com.bootdo.modular.rp.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.enums.BillType;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.data.service.AccountService;
import com.bootdo.modular.data.service.ConsumerService;
import com.bootdo.modular.data.service.VendorService;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.rp.convert.RPOrderConverter;
import com.bootdo.modular.rp.dao.RPOrderEntryDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.rp.param.RPOrderEntryVO;
import com.bootdo.modular.rp.param.RPOrderSettleVO;
import com.bootdo.modular.rp.param.RPOrderVO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.service.impl.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class RPOrderEntryService extends ServiceImpl<RPOrderEntryDao, RPOrderEntryDO> {
    @Resource
    private RPOrderService rpOrderService;
    @Resource
    private RPOrderSettleService rpOrderSettleService;
    @Resource
    private ConsumerService consumerService;
    @Resource
    private VendorService vendorService;
    @Resource
    private UserService userService;
    @Resource
    private AccountService accountService;


    @Transactional(rollbackFor = Exception.class)
    public RPOrderDO save(RPOrderVO orderVO) {
        //结算账户
        Set<String> settleSet = orderVO.getSettleVOList().stream().map(RPOrderSettleVO::getSettleAccount).collect(Collectors.toSet());
        //收款单取客户信息，付款单取供应商信息
        String detectName = "";
        if (BillType.CW_SK_ORDER.equals(orderVO.getBillType())) {
            ConsumerDO consumerDO = consumerService.getByNo(orderVO.getDebtorId());
            detectName = consumerDO.getName();
        } else {
            VendorDO vendorDO = vendorService.getByNo(orderVO.getDebtorId());
            detectName = vendorDO.getName();
        }

        UserDO userDO = userService.getById(orderVO.getCheckId());
        List<AccountDO> accountDOList = accountService.list(Wrappers.lambdaQuery(AccountDO.class).in(AccountDO::getNo, settleSet));
        RPOrderDO orderDO = RPOrderConverter.convertOrder(orderVO, userDO, detectName);

        List<RPOrderEntryDO> orderEntryDOList = RPOrderConverter.convertOrderEntry(orderVO, orderDO);
        List<RPOrderSettleDO> orderSettleDOList = RPOrderConverter.convertOrderSettle(orderVO, orderDO, RPOrderConverter.convertAccountMap(accountDOList));

        //订单入库
        rpOrderService.save(orderDO);
        //收款结算明细入库
        rpOrderSettleService.remove(Wrappers.lambdaQuery(RPOrderSettleDO.class).eq(RPOrderSettleDO::getBillNo, orderDO.getBillNo()));
        rpOrderSettleService.saveBatch(orderSettleDOList);
        //源单核销金额入库
        this.remove(Wrappers.lambdaQuery(RPOrderEntryDO.class).eq(RPOrderEntryDO::getBillNo, orderDO.getBillNo()));
        this.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public RPOrderVO getOrderVO(OrderDetailParam param) {
        RPOrderDO orderDO = rpOrderService.getOne(Wrappers.lambdaQuery(RPOrderDO.class).eq(RPOrderDO::getBillNo, param.getBillNo()));

        if (ObjectUtil.isEmpty(orderDO)) {
            return new RPOrderVO();
        }

        List<RPOrderEntryDO> orderEntryDOList = this.list(Wrappers.lambdaQuery(RPOrderEntryDO.class).eq(RPOrderEntryDO::getBillNo, param.getBillNo()));
        List<RPOrderSettleDO> orderSettleDOList = rpOrderSettleService.list(Wrappers.lambdaQuery(RPOrderSettleDO.class).eq(RPOrderSettleDO::getBillNo, param.getBillNo()));

        RPOrderVO orderVO = new RPOrderVO();
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
