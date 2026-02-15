package com.bootdo.modular.cashier.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.cashier.dao.SettleDao;
import com.bootdo.modular.cashier.result.JournalGeneralResult.SettleOrderItem;
import com.bootdo.modular.cashier.result.SettleYear;
import com.bootdo.modular.cashier.result.SettleYear.SettleYearFlow;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 订单核销
 *
 * @author L
 * @since 2025-02-17 11:19
 */
@Service
public class SettleService extends ServiceImpl<RPOrderDao, RPOrderDO> {

    @Resource
    private SettleDao settleDao;

    @Resource
    private AccountDao accountDao;


    public SettleYear flowSettleYear(Map<String, Object> param) {
        SettleYear settleYear = new SettleYear();
        // 结算账户列表
        List<AccountDO> accountDOList = accountDao.selectList(Wrappers.lambdaQuery(AccountDO.class).orderByAsc(AccountDO::getNo));
        // 结算账户 MAP
        Map<String, Integer> accountIndexMap = new HashMap<>();
        IntStream.range(0, accountDOList.size()).forEach(index -> {
            AccountDO accountDO = accountDOList.get(index);
            accountIndexMap.put(accountDO.getNo().toString(), index);
            BeanUtil.setProperty(settleYear, "settleName" + index, accountDO.getName());
        });
        // 核销记录
        List<SettleYearFlow> flowSettleYearList = settleDao.flowSettleYear(param);
        // 核销记录按年度分组
        Map<String, List<SettleYearFlow>> flowSettleYearMap = flowSettleYearList.stream()
                .collect(Collectors.groupingBy(SettleYearFlow::getYear, Collectors.toList()));
        // 核销金额处理
        List<SettleYear.SettleYearItem> settleYearItemList = flowSettleYearMap.entrySet()
                .stream()
                .map(entry -> {
                    SettleYear.SettleYearItem settleYearItem = new SettleYear.SettleYearItem();
                    settleYearItem.setYear(entry.getKey());
                    // 处理各账户核销金额
                    entry.getValue().forEach(settleOrderYear -> {
                        String settleAccount = settleOrderYear.getSettleAccount();
                        Integer index = accountIndexMap.get(settleAccount);
                        BeanUtil.setProperty(settleYearItem, "settleName" + index, settleOrderYear.getSettleName());
                        BeanUtil.setProperty(settleYearItem, "checkAmount" + index, settleOrderYear.getCheckAmount());
                        BeanUtil.setProperty(settleYearItem, "discountAmount" + index, settleOrderYear.getDiscountAmount());
                        // 求合计金额，easypoi的{{#fe:}}命令，不支持在excel模板是配置公式
                        BeanUtil.setProperty(settleYearItem, "checkAmountSum", NumberUtil.add(settleYearItem.getCheckAmountSum(), settleOrderYear.getCheckAmount()));
                        BeanUtil.setProperty(settleYearItem, "discountAmountSum", NumberUtil.add(settleYearItem.getDiscountAmountSum(), settleOrderYear.getDiscountAmount()));
                    });
                    // 实际收款金额
                    settleYearItem.setPaymentAmountSum(NumberUtil.sub(settleYearItem.getCheckAmountSum(), settleYearItem.getDiscountAmountSum()));
                    return settleYearItem;
                })
                .sorted(Comparator.comparing(SettleYear.SettleYearItem::getYear))
                .collect(Collectors.toList());

        settleYear.setSettleYearItemList(settleYearItemList);

        return settleYear;
    }


    public List<SettleOrderItem> generalSettleOrderItem(Map<String, Object> param) {
        return settleDao.generalSettleOrderItem(param);
    }

}
