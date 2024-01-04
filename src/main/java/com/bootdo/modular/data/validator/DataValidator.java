package com.bootdo.modular.data.validator;

import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.data.dao.*;
import com.bootdo.modular.data.domain.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yogiCai
 * @date 2018-02-04 10:07:05
 */
@Service
public class DataValidator {
    @Resource
    private AccountDao accountDao;
    @Resource
    private ConsumerDao consumerDao;
    @Resource
    private ProductDao productDao;
    @Resource
    private StockDao stockDao;
    @Resource
    private VendorDao vendorDao;

    public void validateAccount(AccountDO account) {
        if (accountDao.count(ImmutableMap.of("idNot", account.getId() == null ? "" : account.getId(), "name", account.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "帐户"));
        }
    }

    public void validateConsumer(ConsumerDO consumer) {
        if (consumerDao.count(ImmutableMap.of("idNot", consumer.getId() == null ? "" : consumer.getId(), "name", consumer.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "客户"));
        }
    }

    public void validateProduct(ProductDO product) {
        if (productDao.count(ImmutableMap.of("idNot", product.getId() == null ? "" : product.getId(), "name", product.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "商品"));
        }
    }

    public void validateStock(StockDO stock) {
        if (stockDao.count(ImmutableMap.of("idNot",stock.getId() == null ? "" : stock.getId(), "stockNo", stock.getStockNo())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NO_DUPLICATE, "仓库"));
        }
    }

    public void validateVendor(VendorDO vendor) {
        if (vendorDao.count(ImmutableMap.of("idNot",vendor.getId() == null ? "" : vendor.getId(), "name", vendor.getName())) > 0) {
            throw new BizServiceException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "供应商"));
        }
    }
}