package com.bootdo.modular.rp.convert;

import cn.hutool.core.util.StrUtil;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.core.utils.OrderUtils;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.rp.controller.request.RPOrderEntryVO;
import com.bootdo.modular.rp.controller.request.RPOrderSettleVO;
import com.bootdo.modular.rp.controller.request.RPOrderVO;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @date 2018-03-06 20:54
 **/
public class RPOrderConverter {

    public static List<RPOrderSettleDO> convertOrderSettle(RPOrderVO orderVO, RPOrderDO orderDO, Map<String, AccountDO> accountDOMap) {
        List<RPOrderSettleDO> settleDOList = Lists.newArrayList();
        BigDecimal paymentAmountTotal = BigDecimal.ZERO;
        for (RPOrderSettleVO vo: orderVO.getSettleVOList()) {
            RPOrderSettleDO orderSettleDO = new RPOrderSettleDO();
            orderSettleDO.setBillNo(orderDO.getBillNo());
            orderSettleDO.setSettleAccount(vo.getSettleAccount());
            orderSettleDO.setSettleName(accountDOMap.get(vo.getSettleAccount()).getName());
            orderSettleDO.setPaymentAmount(NumberUtils.toBigDecimal(vo.getPaymentAmount()));
            orderSettleDO.setRemark(vo.getRemark());
            settleDOList.add(orderSettleDO);
            paymentAmountTotal = NumberUtils.add(paymentAmountTotal, vo.getPaymentAmount());
        }
        orderDO.setPaymentAmount(paymentAmountTotal);
        return settleDOList;
    }

    public static List<RPOrderEntryDO> convertOrderEntry(RPOrderVO orderVO, RPOrderDO orderDO) {
        List<RPOrderEntryDO> entryDOList = Lists.newArrayList();
        BigDecimal checkAmountTotal = BigDecimal.ZERO;
        for (RPOrderEntryVO vo: orderVO.getEntryVOList()) {
            RPOrderEntryDO orderEntryDO = new RPOrderEntryDO();
            orderEntryDO.setBillNo(orderDO.getBillNo());
            orderEntryDO.setSrcBillNo(vo.getSrcBillNo());
            orderEntryDO.setSrcBillType(vo.getSrcBillType());
            orderEntryDO.setSrcBillDate(vo.getSrcBillDate());
            orderEntryDO.setSrcTotalAmount(vo.getSrcTotalAmount());
            orderEntryDO.setSrcPaymentAmount(vo.getSrcPaymentAmount());
            orderEntryDO.setCheckAmount(vo.getCheckAmount());
            entryDOList.add(orderEntryDO);

            checkAmountTotal = checkAmountTotal.add(vo.getCheckAmount());
        }
        orderDO.setCheckAmount(checkAmountTotal);
        return entryDOList;
    }

    public static RPOrderDO convertOrder(RPOrderVO orderVO, UserDO usrDO, String detectName) {
        RPOrderDO orderDO = new RPOrderDO();
        orderDO.setBillNo(StrUtil.isEmpty(orderVO.getBillNo()) ? OrderUtils.generateOrderNoCW(orderVO.getBillType()) : orderVO.getBillNo());
        orderDO.setBillType(orderVO.getBillType());
        orderDO.setBillDate(orderVO.getBillDate());
        orderDO.setDebtorId(orderVO.getDebtorId());
        orderDO.setDebtorName(detectName);
        orderDO.setCheckId(orderVO.getCheckId());
        orderDO.setCheckName(usrDO.getName());
        orderDO.setDiscountAmount(orderVO.getDiscountAmount());
        orderDO.setBillSource(BillSource.USER);
        orderDO.setAuditStatus(AuditStatus.NO);
        orderDO.setRemark(orderVO.getRemark());
        return orderDO;
    }

    public static  Map<String, AccountDO> convertAccountMap(List<AccountDO> accountDOList) {
        Map<String, AccountDO> accountDOMap = Maps.newHashMap();
        for (AccountDO accountDO : accountDOList) {
            accountDOMap.put(accountDO.getNo().toString(), accountDO);
        }
        return accountDOMap;
    }

}
