package com.bootdo.excel.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.common.enumeration.BillSource;
import com.bootdo.common.exception.BootServiceExceptionEnum;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.PoiUtil;
import com.bootdo.common.utils.ShiroUtils;
import com.bootdo.data.dao.AccountDao;
import com.bootdo.data.dao.ConsumerDao;
import com.bootdo.data.dao.ProductDao;
import com.bootdo.data.domain.AccountDO;
import com.bootdo.data.domain.ConsumerDO;
import com.bootdo.data.domain.ProductDO;
import com.bootdo.excel.param.OrderImportEntityParam;
import com.bootdo.excel.param.OrderImportParam;
import com.bootdo.se.controller.request.SEOrderEntryVO;
import com.bootdo.se.controller.request.SEOrderVO;
import com.bootdo.se.service.SEOrderEntryService;
import com.bootdo.system.dao.UserDao;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 订单导入
 *
 * @author caiyz
 * @since 2023-03-27 18:07
 */
@Service
public class OrderImportService {

    @Resource
    private UserDao userDao;
    @Resource
    private ConsumerDao consumerDao;
    @Resource
    private ProductDao productDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private SEOrderEntryService seOrderEntryService;


    @Transactional(rollbackFor = Exception.class)
    public void importExcel(OrderImportParam orderImportParam) throws Exception {

        String filename = orderImportParam.getFile().getOriginalFilename();
        String billDateYm = StrUtil.subBefore(filename, StrUtil.C_DOT, true);
        //单据日期校验（文件名规则：单据日期yyyy-MM）
        BootServiceExceptionEnum.BILL_DATE_INVALID.assertIsTrue(DateUtil.formatDate(orderImportParam.getBillDateB()).startsWith(billDateYm), orderImportParam.getBillDateB());
        //结算账户 默认取第一个
        AccountDO accountDo = accountDao.list(MapUtil.empty()).get(0);
        //客户用户信息
        Map<String, ConsumerDO> comsumerDoMap = consumerDao.list(MapUtil.empty()).stream()
                .collect(Collectors.toMap(k -> joinKey(k.getNo(), k.getName()), v -> v, (o, n) -> n));
        //商品信息
        Map<String, ProductDO> productDoMap = productDao.list(MapUtil.empty()).stream()
                .collect(Collectors.toMap(k -> joinKey(k.getNo(), k.getName()), v -> v, (o, n) -> n));
        //导入excel文件
        Workbook hssfWorkbook = PoiUtil.getWorkBook(orderImportParam.getFile());

        List<SEOrderVO> seOrderVoList = new ArrayList<>();
        //订单导入
        for (Date date = orderImportParam.getBillDateB(); date.compareTo(orderImportParam.getBillDateE()) <= 0; date = DateUtil.offsetDay(date, 1)) {

            String sheetName = StrUtil.toString(DateUtil.dayOfMonth(date));
            //是否存在当前单据日期的Sheet（不存在返回-1）
            int startSheetIndex = hssfWorkbook.getSheetIndex(sheetName);
            if (startSheetIndex < 0) {
                continue;
            }
            //读取当前单据日期Sheet数据
            ImportParams importParams = new ImportParams();
            importParams.setStartSheetIndex(startSheetIndex);
            importParams.setSheetNum(1);
            List<OrderImportEntityParam> dataList = ExcelImportUtil.importExcel(orderImportParam.getFile().getInputStream(), OrderImportEntityParam.class, importParams);

            //当前单据日期的订单（一个Sheet）
            SEOrderVO seOrderVo = null;
            for (OrderImportEntityParam entity : dataList) {
                //订单首行（有客户名、优惠金额、支付金额、已付金额）
                if (StrUtil.isNotBlank(entity.getConsumerName())) {
                    //客户是否存在
                    ConsumerDO consumerDo = getConsumer(comsumerDoMap, entity.getConsumerName());
                    BootServiceExceptionEnum.IMPORT_CONSUMER_NOT_FOUND.assertNotNull(consumerDo, sheetName, entity.getConsumerName());

                    seOrderVo = new SEOrderVO();
                    seOrderVo.setBillDate(date);
                    seOrderVo.setConsumerId(consumerDo.getNo().toString());
                    seOrderVo.setConsumerName(entity.getConsumerName());
                    seOrderVo.setDiscountAmountTotal(entity.getDiscountAmount());
                    seOrderVo.setPaymentAmountTotal(entity.getPayAmount());
                    seOrderVo.setSettleAccountTotal(accountDo.getNo().toString());
                    seOrderVo.setBillSource(BillSource.IMPORT);
                    seOrderVo.setRemark(entity.getPaperBillNo());
                    seOrderVo.setBillerId(ShiroUtils.getUserId().toString());
                    seOrderVoList.add(seOrderVo);
                }
                //excel是否都设置了客户名称
                BootServiceExceptionEnum.IMPORT_CONSUMER_NOT_SET.assertNotNull(seOrderVo, sheetName);
                //分录商品信息
                ProductDO productDo = getProduct(productDoMap, entity.getEntryName(), entity.getEntryPrice());
                BootServiceExceptionEnum.IMPORT_PRODUCT_NOT_FOUND.assertNotNull(productDo, sheetName, entity.getEntryName());
                //订单分录明细
                SEOrderEntryVO seOrderEntryVO = new SEOrderEntryVO();
                seOrderEntryVO.setEntryId(productDo.getNo().toString());
                seOrderEntryVO.setEntryName(productDo.getName());
                seOrderEntryVO.setEntryUnit(productDo.getUnit());
                seOrderEntryVO.setEntryPrice(entity.getEntryPrice());
                seOrderEntryVO.setStockNo(productDo.getStockNo());
                seOrderEntryVO.setStockName(productDo.getStockName());
                seOrderEntryVO.setTotalQty(entity.getTotalQty());
                seOrderEntryVO.setEntryAmount(NumberUtils.mul(entity.getTotalQty(), entity.getEntryPrice()));
                seOrderEntryVO.setTotalAmount(NumberUtils.mul(entity.getTotalQty(), entity.getEntryPrice()));
                seOrderVo.getEntryVOList().add(seOrderEntryVO);
            }
            //统计订单金额
            seOrderVoList.forEach(seOrder -> {
                //商品金额、优惠率、优惠后商品金额（商品金额 - 优惠金额）
                BigDecimal entryAmountTotal = seOrder.getEntryVOList().stream().map(SEOrderEntryVO::getEntryAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal discountAmountTotal = seOrder.getDiscountAmountTotal();
                BigDecimal discountRateTotal = NumberUtils.div(discountAmountTotal, entryAmountTotal);
                BigDecimal finalAmountTotal = NumberUtils.subtract(entryAmountTotal, discountAmountTotal);

                seOrder.setEntryAmountTotal(entryAmountTotal);
                seOrder.setDiscountRateTotal(discountRateTotal);
                seOrder.setFinalAmountTotal(finalAmountTotal);
                //采购单入库
                seOrderEntryService.save(seOrder);
            });
        }
    }

    /**
     * 根据客户名查询客户信息
     * 根据客户名是否包括，存在多个时默认取第一个
     */
    private <T> T getConsumer(Map<String, T> userDoMap, String consumerName) {
        Optional<Map.Entry<String, T>> optionalUserDO = userDoMap.entrySet().stream()
                .filter(entry -> StrUtil.contains(entry.getKey(), consumerName)).findFirst();
        return optionalUserDO.map(Map.Entry::getValue).orElse(null);
    }

    /**
     * 根据客户简称查询商品信息
     * 根据商品名是否包括，存在多个时默认取第一个取零售价最接近的那个商品
     */
    private ProductDO getProduct(Map<String, ProductDO> productDoMap, String productName, BigDecimal entityPrice) {
        List<ProductDO> productList = productDoMap.entrySet().stream()
                .filter(entry -> StrUtil.contains(entry.getKey(), productName))
                .map(Map.Entry::getValue)
                .sorted((o1, o2) -> {
                    double offset1 = Math.abs(entityPrice.subtract(o1.getSalePrice()).doubleValue());
                    double offset2 = Math.abs(entityPrice.subtract(o2.getSalePrice()).doubleValue());
                    return NumberUtil.compare(offset1, offset2);
                })
                .collect(Collectors.toList());

        return productList.isEmpty() ? null : productList.get(0);
    }

    /**
     * 业务标识
     */
    private String joinKey(Object... objs) {
        return StrUtil.join(StrUtil.UNDERLINE, objs);
    }

}
