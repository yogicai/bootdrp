package com.bootdo.modular.excel.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.excel.ClassExcelVerifyHandler;
import com.bootdo.core.excel.enums.VerifyResultEnum;
import com.bootdo.core.exception.BootServiceExceptionEnum;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.dao.ConsumerDao;
import com.bootdo.modular.data.dao.ProductDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.excel.param.OrderImportEntityParam;
import com.bootdo.modular.excel.param.OrderImportParam;
import com.bootdo.modular.se.param.SEOrderEntryVO;
import com.bootdo.modular.se.param.SEOrderVO;
import com.bootdo.modular.se.service.SEOrderEntryService;
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
 * @author L
 * @since 2023-03-27 18:07
 */
@Service
public class OrderImportService {
    @Resource
    private ConsumerDao consumerDao;
    @Resource
    private ProductDao productDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private SEOrderEntryService seOrderEntryService;
    @Resource
    private ClassExcelVerifyHandler classExcelVerifyHandler;


    @Transactional(rollbackFor = Exception.class)
    public void importExcel(OrderImportParam orderImportParam) throws Exception {

        String filename = orderImportParam.getFile().getOriginalFilename();
        String billDateYm = DateUtil.format(orderImportParam.getBillDateB(), "yyyy-MM");
        //单据日期校验（文件名规则：单据日期yyyy-MM）
        boolean billDateFlag = StrUtil.startWith(filename, billDateYm) && DateUtil.formatDate(orderImportParam.getBillDateB()).startsWith(billDateYm);
        BootServiceExceptionEnum.BILL_DATE_INVALID.assertIsTrue(billDateFlag, orderImportParam.getBillDateB());
        //结算账户 默认取第一个
        AccountDO accountDo = accountDao.list(MapUtil.empty()).get(0);
        //客户用户信息
        Map<String, ConsumerDO> comsumerDoMap = consumerDao.list(MapUtil.empty()).stream()
                .collect(Collectors.toMap(k -> joinKey(k.getNo(), k.getName()), v -> v, (o, n) -> n));
        //商品信息
        Map<String, ProductDO> productDoMap = productDao.list(MapUtil.of("status", 1)).stream()
                .collect(Collectors.toMap(k -> joinKey(k.getNo(), k.getName()), v -> v, (o, n) -> n));
        //导入excel文件
        Workbook hssfWorkbook = PoiUtil.getWorkBook(orderImportParam.getFile());

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
            importParams.setVerifyHandler(classExcelVerifyHandler);
            importParams.setStartSheetIndex(startSheetIndex);
            importParams.setSheetNum(1);
            //开启Excel校验
            importParams.setNeedVerify(true);

            ExcelImportResult<OrderImportEntityParam> result = ExcelImportUtil.importExcelMore(orderImportParam.getFile().getInputStream(), OrderImportEntityParam.class, importParams);

            String errorMsg = result.getFailList().stream()
                    .filter(o -> !VerifyResultEnum.ROW_NULL.equals(o.getVerifyResultEnum()))
                    .map(o -> StrUtil.format(Constant.IMPORT_ORDER_INVALID, o.getRowNum() + 1, o.getErrorMsg()))
                    .collect(Collectors.joining(Constant.HTML_BR));

            //非空行类型校验失败
            boolean isVerifyFail = result.isVerifyFail() && result.getFailList().stream().anyMatch(o -> !VerifyResultEnum.ROW_NULL.equals(o.getVerifyResultEnum()));
            BootServiceExceptionEnum.IMPORT_ORDER_NOT_VALID.assertIsFalse(isVerifyFail, sheetName, errorMsg);

            //当前单据日期的订单（一个Sheet）
            SEOrderVO seOrderVo = null;
            List<SEOrderVO> seOrderVoList = new ArrayList<>();

            for (OrderImportEntityParam entity : result.getList()) {
                //订单首行（有客户名、优惠金额、支付金额、已付金额）
                if (StrUtil.isNotBlank(entity.getConsumerName())) {
                    //客户是否存在
                    ConsumerDO consumerDo = getConsumer(comsumerDoMap, entity.getConsumerName());
                    BootServiceExceptionEnum.IMPORT_CONSUMER_NOT_FOUND.assertNotNull(consumerDo, sheetName, entity.getConsumerName());

                    seOrderVo = new SEOrderVO();
                    seOrderVo.setBillDate(date);
                    seOrderVo.setConsumerId(consumerDo.getNo().toString());
                    seOrderVo.setConsumerName(entity.getConsumerName());
                    seOrderVo.setDiscountAmountTotal(ObjUtil.defaultIfNull(entity.getDiscountAmount(), BigDecimal.ZERO));
                    seOrderVo.setPaymentAmountTotal(ObjUtil.defaultIfNull(entity.getPayAmount(), BigDecimal.ZERO));
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
                .filter(entry -> StrUtil.contains(StrUtil.cleanBlank(entry.getKey()), consumerName)).findFirst();
        return optionalUserDO.map(Map.Entry::getValue).orElse(null);
    }

    /**
     * 根据客户简称查询商品信息
     * 根据商品名是否包括，存在多个时默认取第一个取零售价最接近的那个商品
     */
    private ProductDO getProduct(Map<String, ProductDO> productDoMap, String productName, BigDecimal entityPrice) {
        List<ProductDO> productList = productDoMap.entrySet().stream()
                .filter(entry -> StrUtil.contains(StrUtil.cleanBlank(entry.getKey()), productName))
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


    /**
     * 销售单模板下载
     */
    public void exportTpl() {
        //sheet页数
        int sheetNum = 31;
        //文件名
        String fileName = DateUtil.format(new Date(), "yyyy-MM") + ".xls";

        List<List<?>> dateList = new ArrayList<>(sheetNum);
        List<String> sheetNameList = new ArrayList<>(sheetNum);
        List<Class<?>> pojoClassList = new ArrayList<>(sheetNum);
        OrderImportEntityParam entityParam = new OrderImportEntityParam();

        for (int i = 1; i <= sheetNum; i++) {
            dateList.add(CollUtil.newLinkedList(entityParam));
            sheetNameList.add(StrUtil.toString(i));
            pojoClassList.add(OrderImportEntityParam.class);
        }

        PoiUtil.exportExcelWithStream(fileName, pojoClassList, dateList, sheetNameList);
    }

}
