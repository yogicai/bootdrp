package com.bootdo.modular.data.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.data.domain.*;
import com.bootdo.modular.data.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yogiCai
 * @since 2018-02-04 10:07:05
 */
@Service
public class DataValidator {
    @Resource
    private AccountService accountService;
    @Resource
    private ConsumerService consumerService;
    @Resource
    private ProductService productService;
    @Resource
    private StockService stockService;
    @Resource
    private VendorService vendorService;


    public void validateAccount(AccountDO account) {
        if (accountService.count(Wrappers.lambdaQuery(AccountDO.class).ne(AccountDO::getId, account.getId()).eq(AccountDO::getName, account.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "帐户"));
        }
    }

    public void validateConsumer(ConsumerDO consumer) {
        if (consumerService.count(Wrappers.lambdaQuery(ConsumerDO.class).ne(ConsumerDO::getId, consumer.getId()).eq(ConsumerDO::getName, consumer.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "客户"));
        }
    }

    public void validateProduct(ProductDO product) {
        if (productService.count(Wrappers.lambdaQuery(ProductDO.class).ne(ProductDO::getId, product.getId()).eq(ProductDO::getName, product.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "商品"));
        }
    }

    public void validateStock(StockDO stock) {
        if (stockService.count(Wrappers.lambdaQuery(StockDO.class).ne(StockDO::getId, stock.getId()).eq(StockDO::getStockName, stock.getStockName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "仓库"));
        }
    }

    public void validateVendor(VendorDO vendor) {
        if (vendorService.count(Wrappers.lambdaQuery(VendorDO.class).ne(VendorDO::getId, vendor.getId()).eq(VendorDO::getName, vendor.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "供应商"));
        }
    }
    
}