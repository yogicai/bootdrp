package com.bootdo.modular.cashier.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.CharsetDetector;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.cashier.dao.RecordDao;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.param.RecordQryParam;
import com.bootdo.modular.cashier.result.MultiSelect;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 日记账
 *
 * @author L
 * @since 2022-06-23 13:30
 */
@Service
public class RecordService extends ServiceImpl<RecordDao, RecordDO> {
    @Resource
    private RecordDao recordDao;

    private static final String COMMA = "：";
    private static final String WX_NAME = "微信昵称：";
    private static final String ALIPAY = "alipay";
    private static final String ALIPAY_NAME = "姓名：";
    private static final String ALIPAY_ACCOUNT = "支付宝账户：";
    private static final String TRADE_TIME = "起始时间：";
    private static final Pattern TRADE_TIME_PATTERN = Pattern.compile("起始时间：\\[(.*)\\]\\s+终止时间：\\[(.*)\\]");


    public PageJQ page(RecordQryParam param) {
        Page<RecordDO> page = recordDao.list(PageFactory.defaultPage(), BeanUtil.beanToMap(param));
        Map<String, Object> map = recordDao.selectSum(BeanUtil.beanToMap(param));
        return new PageJQ(page, MapUtil.getInt(map, "totalAmount", 0));
    }

    public List<RecordDO> list(Map<String, Object> param) {
        return recordDao.list(param);
    }

    public void export(RecordQryParam param) {
        List<RecordDO> orderList = recordDao.list(BeanUtil.beanToMap(param));
        String fileName = StrUtil.format("支付对账单{}-{}.xlsx", DateUtil.format(param.getStart(), DatePattern.PURE_DATE_FORMAT), DateUtil.format(param.getEnd(), DatePattern.PURE_DATE_FORMAT));
        PoiUtil.exportExcelWithStream(fileName, RecordDO.class, orderList);
    }

    public void importCvs(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (StrUtil.startWith(filename, ALIPAY)) {
            readAliCvs(file);
        } else {
            readWxCvs(file);
        }
    }

    private void readAliCvs(MultipartFile file) throws Exception {
        List<RecordDO> recordDOList = new ArrayList<>();
        // 获取字符编码
        Charset charset = CharsetDetector.detect(file.getInputStream());
        //获取csv文件读取器
        CsvData csvData = CsvUtil.getReader().read(new InputStreamReader(file.getInputStream(), charset));
        //文件标题
        RecordDO recordHead = new RecordDO();

        LambdaQueryWrapper<RecordDO> queryWrapper = Wrappers.lambdaQuery(RecordDO.class);

        csvData.getRows().forEach(row -> {
            if (row.getFieldCount() == 1 && StrUtil.startWith(row.get(0), ALIPAY_NAME)) {
                String nick = StrUtil.splitToArray(row.get(0), COMMA)[1];
                recordHead.setNick(nick);
                queryWrapper.eq(RecordDO::getNick, nick);
            } else if (row.getFieldCount() == 1 && StrUtil.startWith(row.get(0), ALIPAY_ACCOUNT)) {
                String account = StrUtil.splitToArray(row.get(0), COMMA)[1];
                recordHead.setAccount(account);
                queryWrapper.eq(RecordDO::getAccount, account);
            } else if (row.getFieldCount() == 1 && StrUtil.startWith(row.get(0), TRADE_TIME)) {
                Matcher matcher = TRADE_TIME_PATTERN.matcher(row.get(0));
                boolean b = matcher.matches();
                queryWrapper.ge(RecordDO::getTradeTime, b ? matcher.group(1) : StrUtil.EMPTY)
                        .le(RecordDO::getTradeTime, b ? matcher.group(2) : StrUtil.EMPTY);
            } else if (row.getFieldCount() > 5 && !StrUtil.startWith(row.get(0), "收/支")) {
                RecordDO recordDO = new RecordDO();
                recordDO.setType("支付宝");
                recordDO.setPayDirect(trimStr(row.get(0)));
                recordDO.setTargetName(trimStr(row.get(1)));
                recordDO.setTargetAccount(trimStr(row.get(2)));
                recordDO.setTradeGoods(trimStr(row.get(3)));
                recordDO.setTradeType(trimStr(row.get(4)));
                recordDO.setPayAmount(NumberUtil.parseDouble(row.get(5)));
                recordDO.setPayStatus(trimStr(row.get(6)));
                recordDO.setTradeClass(trimStr(row.get(7)));
                recordDO.setTxnNo(trimStr(row.get(8)));
                recordDO.setBizNo(trimStr(row.get(9)));
                recordDO.setTradeTime(DateUtil.parse(row.get(10)));
                recordDO.setRemark(StrUtil.EMPTY);
                recordDO.setCostType(trimStr(row.get(11)));
                recordDO.setSource("对账单导入");
                recordDOList.add(recordDO);
            }
        });
        recordDOList.forEach(recordDO -> {
            recordDO.setNick(recordHead.getNick());
            recordDO.setAccount(recordHead.getAccount());
        });
        if (CollUtil.isNotEmpty(recordDOList)) {
            this.remove(queryWrapper);
            this.saveBatch(recordDOList);
        }
    }

    private void readWxCvs(MultipartFile file) throws Exception {
        List<RecordDO> recordDOList = new ArrayList<>();
        //获取字符编码
        Charset charset = CharsetDetector.detect(file.getInputStream());
        //获取csv文件读取器
        CsvData csvData = CsvUtil.getReader().read(new InputStreamReader(file.getInputStream(), charset));
        //文件标题
        RecordDO recordHead = new RecordDO();

        LambdaQueryWrapper<RecordDO> queryWrapper = Wrappers.lambdaQuery(RecordDO.class);

        csvData.getRows().forEach(row -> {
            if (row.getOriginalLineNumber() == 1 && StrUtil.startWith(row.get(0), WX_NAME)) {
                String nick = StrUtil.splitToArray(row.get(0), COMMA)[1];
                recordHead.setNick(nick);
                recordHead.setAccount(nick);
                queryWrapper.eq(RecordDO::getNick, nick).eq(RecordDO::getAccount, nick);
            } else if (row.getOriginalLineNumber() == 2 && StrUtil.startWith(row.get(0), TRADE_TIME)) {
                Matcher matcher = TRADE_TIME_PATTERN.matcher(row.get(0));
                boolean b = matcher.matches();
                queryWrapper.ge(RecordDO::getTradeTime, b ? matcher.group(1) : StrUtil.EMPTY)
                        .le(RecordDO::getTradeTime, b ? matcher.group(2) : StrUtil.EMPTY);
            } else if (row.getOriginalLineNumber() >= 17) {
                RecordDO recordDO = new RecordDO();
                recordDO.setType("微信");
                recordDO.setTradeTime(DateUtil.parse(row.get(0)));
                recordDO.setTradeClass(trimStr(row.get(1)));
                recordDO.setTargetName(trimStr(row.get(2)));
                recordDO.setTargetAccount(trimStr(row.get(2)));
                recordDO.setTradeGoods(trimStr(row.get(3)));
                recordDO.setPayDirect(trimStr(row.get(4)));
                recordDO.setPayAmount(NumberUtil.parseDouble(StrUtil.removeAny(row.get(5), "¥")));
                recordDO.setTradeType(trimStr(row.get(6)));
                recordDO.setPayStatus(trimStr(row.get(7)));
                recordDO.setTxnNo(trimStr(row.get(8)));
                recordDO.setBizNo(trimStr(row.get(9)));
                recordDO.setRemark(trimStr(row.get(10)));
                recordDO.setCostType(trimStr(row.get(11)));
                recordDO.setSource("对账单导入");
                recordDOList.add(recordDO);
            }
        });
        recordDOList.forEach(recordDO -> {
            recordDO.setNick(recordHead.getNick());
            recordDO.setAccount(recordHead.getAccount());
        });
        if (CollUtil.isNotEmpty(recordDOList)) {
            this.remove(queryWrapper);
            this.saveBatch(recordDOList);
        }
    }

    private String trimStr(String str) {
        return StrUtil.blankToDefault(StrUtil.removeAny(StrUtil.trim(str), StrUtil.TAB), StrUtil.SLASH);
    }

    public MultiSelect multiSelect() {
        MultiSelect multiSelect = new MultiSelect();
        List<RecordDO> list = recordDao.multiSelect(MapUtil.empty());
        list.forEach(recordDO -> {
            multiSelect.getType().putIfAbsent(recordDO.getType(), recordDO.getType());
            multiSelect.getAccount().putIfAbsent(recordDO.getAccount(), recordDO.getAccount());
            multiSelect.getPayDirect().putIfAbsent(recordDO.getPayDirect(), recordDO.getPayDirect());
            multiSelect.getPayStatus().putIfAbsent(recordDO.getPayStatus(), recordDO.getPayStatus());
            multiSelect.getTradeClass().putIfAbsent(recordDO.getTradeClass(), recordDO.getTradeClass());
            multiSelect.getSource().putIfAbsent(recordDO.getSource(), recordDO.getSource());
            multiSelect.getCostType().putIfAbsent(recordDO.getCostType(), recordDO.getCostType());
        });
        return multiSelect;
    }

}
