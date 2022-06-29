package com.bootdo.cashier.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.cashier.controller.response.MultiSelect;
import com.bootdo.cashier.dao.RecordDao;
import com.bootdo.cashier.domain.JournalDO;
import com.bootdo.cashier.domain.RecordDO;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.R;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/** 
 * 日记账
 * 
 * @author caiyz
 * @since 2022-06-23 13:30
 */
@Service
public class RecordService {
	@Resource
	private RecordDao recordDao;

	private static final String COMMA = "：";
	private static final String WX_NAME = "微信昵称：";
	private static final String ALIPAY = "alipay";
	private static final String ALIPAY_NAME = "姓名：";
	private static final String ALIPAY_ACCOUNT = "支付宝账户：";
	private static final String TRADE_TIME = "起始时间：";
	private static final Pattern TRADE_TIME_PATTERN = Pattern.compile("起始时间：\\[(.*)\\]\\s+终止时间：\\[(.*)\\]");


	public RecordDO get(Integer id){
		return recordDao.get(id);
	}
	
	public List<RecordDO> list(Map<String, Object> map){
		return recordDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return recordDao.count(map);
	}
	
	public Map<String,Object> selectSum(Map<String, Object> map){
		return recordDao.selectSum(map);
	}

	public int save(RecordDO recordDO){
		return recordDao.save(recordDO);
	}
	
	public int update(RecordDO recordDO){
		return recordDao.update(recordDO);
	}
	
	public int remove(Map<String, Object> param){
		return recordDao.remove(param);
	}

	public int batchRemove(Integer[] ids){
		return recordDao.batchRemove(ids);
	}

	@Transactional(rollbackOn = Exception.class)
	public void importCvs(MultipartFile file) throws Exception {
		String filename = file.getOriginalFilename();
		if (StrUtil.startWith(filename, ALIPAY)) {
			readAliCvs(file);
		} else {
			readWxCvs(file);
		}
	}

	private void readAliCvs(MultipartFile file) throws Exception {
		Map<String, Object> param = new HashMap<>();
		List<RecordDO> recordDOList = new ArrayList<>();
		CsvData csvData = CsvUtil.getReader().read(new InputStreamReader(file.getInputStream(), CharsetUtil.GBK));
		RecordDO recordHead = new RecordDO();
		csvData.getRows().forEach(row -> {
			if (row.getFieldCount() == 1 && StrUtil.startWith(row.get(0), ALIPAY_NAME)) {
				String nick = StrUtil.split(row.get(0), COMMA)[1];
				recordHead.setNick(nick);
				param.put("nick", nick);
			} else if (row.getFieldCount() == 1 && StrUtil.startWith(row.get(0), ALIPAY_ACCOUNT)) {
				String account = StrUtil.split(row.get(0), COMMA)[1];
				recordHead.setAccount(account);
				param.put("account", account);
			} else if (row.getFieldCount() == 1 && StrUtil.startWith(row.get(0), TRADE_TIME)) {
				Matcher matcher = TRADE_TIME_PATTERN.matcher(row.get(0));
				boolean b = matcher.matches();
				param.put("start", b ? matcher.group(1) : StrUtil.EMPTY);
				param.put("end",  b ? matcher.group(2) : StrUtil.EMPTY);
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
				recordDO.setSource("对账单导入");
				recordDOList.add(recordDO);
			}
		});
		recordDOList.forEach(recordDO -> {
			recordDO.setNick(recordHead.getNick());
			recordDO.setAccount(recordHead.getAccount());
		});
		if (CollectionUtil.isNotEmpty(recordDOList)) {
			recordDao.remove(param);
			recordDao.saveBatch(recordDOList);
		}
	}

	private void readWxCvs(MultipartFile file) throws Exception {
		Map<String, Object> param = new HashMap<>();
		List<RecordDO> recordDOList = new ArrayList<>();
		CsvData csvData = CsvUtil.getReader().read(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
		RecordDO recordHead = new RecordDO();
		csvData.getRows().forEach(row -> {
			if (row.getOriginalLineNumber() == 2 && StrUtil.startWith(row.get(0), WX_NAME)) {
				String nick = StrUtil.split(row.get(0), COMMA)[1];
				recordHead.setNick(nick);
				recordHead.setAccount(nick);
				param.put("nick", nick);
				param.put("account", nick);
			} else if (row.getOriginalLineNumber() == 3 && StrUtil.startWith(row.get(0), TRADE_TIME)) {
				Matcher matcher = TRADE_TIME_PATTERN.matcher(row.get(0));
				boolean b = matcher.matches();
				param.put("start", b ? matcher.group(1) : StrUtil.EMPTY);
				param.put("end",  b ? matcher.group(2) : StrUtil.EMPTY);
			} else if (row.getOriginalLineNumber() >= 18) {
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
				recordDO.setSource("对账单导入");
				recordDOList.add(recordDO);
			}
		});
		recordDOList.forEach(recordDO -> {
			recordDO.setNick(recordHead.getNick());
			recordDO.setAccount(recordHead.getAccount());
		});
		if (CollectionUtil.isNotEmpty(recordDOList)) {
			recordDao.remove(param);
			recordDao.saveBatch(recordDOList);
		}
	}

	private String trimStr(String str) {
		return StrUtil.removeAny(StrUtil.trim(str), StrUtil.TAB);
	}

	public MultiSelect multiSelect() {
		MultiSelect multiSelect = new MultiSelect();
		List<RecordDO> list = recordDao.multiSelect(null);
		list.forEach(recordDO -> {
			multiSelect.getType().putIfAbsent(recordDO.getType(), recordDO.getType());
			multiSelect.getAccount().putIfAbsent(recordDO.getAccount(), recordDO.getAccount());
			multiSelect.getPayDirect().putIfAbsent(recordDO.getPayDirect(), recordDO.getPayDirect());
			multiSelect.getPayStatus().putIfAbsent(recordDO.getPayStatus(), recordDO.getPayStatus());
			multiSelect.getTradeClass().putIfAbsent(recordDO.getTradeClass(), recordDO.getTradeClass());
			multiSelect.getSource().putIfAbsent(recordDO.getSource(), recordDO.getSource());
		});
		return multiSelect;
	}

}
