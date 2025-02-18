package com.bootdo.modular.cashier.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.cashier.dao.SettleDao;
import com.bootdo.modular.cashier.result.JournalGeneralResult.SettleOrderItem;
import com.bootdo.modular.cashier.result.SettleYear;
import com.bootdo.modular.cashier.result.SettleYear.SettleYearItem;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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


    public SettleYear flowSettleYear( Map<String, Object> param) {
        SettleYear settleYear = new SettleYear();
        ////结算账户列表
        List<AccountDO> accountDOList = accountDao.selectList(Wrappers.lambdaQuery(AccountDO.class).orderByAsc(AccountDO::getNo));
        //结算账户MAP
        Map<String, Integer> accountIndexMap = new HashMap<>();
        IntStream.range(0, accountDOList.size()).forEach(index -> {
            AccountDO accountDO = accountDOList.get(index);
            accountIndexMap.put(accountDO.getNo().toString(), index);
            BeanUtil.setProperty(settleYear, "settleName" + index, accountDO.getName());
        });
        //核销记录
        List<Map<String, Object>> flowSettleYearList = settleDao.flowSettleYear(param);
        //核销记录按年度分组
        Map<String, List<Map<String, Object>>> flowSettleYearMap = flowSettleYearList.stream()
                .collect(Collectors.groupingBy(m -> MapUtil.getStr(m, "year"), Collectors.toList()));
        //核销金额处理
        List<SettleYearItem> settleYearItemList = flowSettleYearMap.entrySet()
                .stream()
                .map(entry -> {
                    SettleYearItem settleYearItem = new SettleYearItem();
                    settleYearItem.setYear(entry.getKey());
                    //处理各账户核销金额
                    entry.getValue().forEach(m -> {
                        String settleAccount = MapUtil.getStr(m, "settleAccount");
                        Integer index = accountIndexMap.get(settleAccount);
                        BeanUtil.setProperty(settleYearItem, "settleName" + index, MapUtil.getStr(m, "settleName"));
                        BeanUtil.setProperty(settleYearItem, "checkAmount" + index, MapUtil.getStr(m, "checkAmount"));
                        BeanUtil.setProperty(settleYearItem, "discountAmount" + index, MapUtil.getStr(m, "discountAmount"));
                        //求合计金额，easypoi的{{#fe:}}命令，不支持在excel模板是配置公式
                        BeanUtil.setProperty(settleYearItem, "checkAmountSum", NumberUtil.add(settleYearItem.getCheckAmountSum(), MapUtil.get(m, "checkAmount", BigDecimal.class)));
                        BeanUtil.setProperty(settleYearItem, "discountAmountSum", NumberUtil.add(settleYearItem.getDiscountAmountSum(), MapUtil.get(m, "discountAmount", BigDecimal.class)));
                    });
                    //实际收款金额
                    settleYearItem.setPaymentAmountSum(NumberUtil.sub(settleYearItem.getCheckAmountSum(), settleYearItem.getDiscountAmountSum()));
                    return settleYearItem;
                })
                .sorted(Comparator.comparing(SettleYearItem::getYear))
                .collect(Collectors.toList());

        settleYear.setSettleYearItemList(settleYearItemList);

        return settleYear;
    }


    public List<SettleOrderItem> generalSettleOrderItem(Map<String, Object> param) {
        return settleDao.generalSettleOrderItem(param);
    }

}
