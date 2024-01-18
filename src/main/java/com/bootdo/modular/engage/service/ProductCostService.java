package com.bootdo.modular.engage.service;

import cn.hutool.core.bean.BeanUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.CostType;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.engage.dao.ProductCostDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 * @since 2022-03-2 14:10
 */
@Service
public class ProductCostService {

    @Resource
    private ProductCostDao productCostDao;


    public ProductCostDO get(Integer id) {
        return productCostDao.get(id);
    }

    public List<ProductCostDO> costList(Map<String, Object> map) {
        return productCostDao.costList(map);
    }

    public int costCount(Map<String, Object> map) {
        return productCostDao.costCount(map);
    }

    public int adjust(ProductCostDO productCost) {
        ProductCostDO productCostDO = productCostDao.get(productCost.getId());
        ProductCostDO productCostDO1 = BeanUtil.copyProperties(productCostDO, ProductCostDO.class, "id");

        productCostDO1.setCostQty(productCost.getCostQty());
        productCostDO1.setCostPrice(productCost.getCostPrice());
        productCostDO1.setCostDate(DateUtils.nowDate());
        productCostDO1.setCostType(CostType.MANUAL.name());
        productCostDO1.setRemark(String.format(Constant.COST_REMARK, CostType.MANUAL.getRemark(), ShiroUtils.getUser().getUsername()));
        return productCostDao.save(productCostDO1);
    }


}
