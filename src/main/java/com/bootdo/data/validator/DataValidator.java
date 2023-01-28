package com.bootdo.data.validator;

import com.bootdo.common.constants.ErrorMessage;
import com.bootdo.common.constants.OrderStatusCode;
import com.bootdo.common.exception.BusinessException;
import com.bootdo.data.dao.*;
import com.bootdo.data.domain.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yogiCai
 * @date 2018-02-04 10:07:05
 */
@Service
public class DataValidator {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ConsumerDao consumerDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private StockDao stockDao;
    @Autowired
    private VendorDao vendorDao;

    public void validateAccount(AccountDO account) {
        if (accountDao.count(ImmutableMap.of("idNot", account.getId() == null ? "" : account.getId(), "name", account.getName())) > 0) {
            throw new BusinessException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "帐户"));
        }
    }

    public void validateConsumer(ConsumerDO consumer) {
        if (consumerDao.count(ImmutableMap.of("idNot", consumer.getId() == null ? "" : consumer.getId(), "name", consumer.getName())) > 0) {
            throw new BusinessException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "客户"));
        }
    }

    public void validateProduct(ProductDO product) {
        if (productDao.count(ImmutableMap.of("idNot", product.getId() == null ? "" : product.getId(), "name", product.getName())) > 0) {
            throw new BusinessException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "商品"));
        }
    }

    public void validateStock(StockDO stock) {
        if (stockDao.count(ImmutableMap.of("idNot",stock.getId() == null ? "" : stock.getId(), "stockNo", stock.getStockNo())) > 0) {
            throw new BusinessException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NO_DUPLICATE, "仓库"));
        }
    }

    public void validateVendor(VendorDO vendor) {
        if (vendorDao.count(ImmutableMap.of("idNot",vendor.getId() == null ? "" : vendor.getId(), "name", vendor.getName())) > 0) {
            throw new BusinessException(OrderStatusCode.DATA_INVALID, String.format(ErrorMessage.DATA_NAME_DUPLICATE, "供应商"));
        }
    }
}