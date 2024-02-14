package com.bootdo.modular.engage.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.CostType;
import com.bootdo.core.enums.CostVersion;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.engage.dao.ProductCostDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.param.ProductCostQryParam;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author L
 * @since 2022-03-2 14:10
 */
@Service
public class ProductCostService extends ServiceImpl<ProductCostDao, ProductCostDO> {

    public PageJQ page(ProductCostQryParam param) {
        return new PageJQ(this.pageListCost(PageFactory.defaultPage(), param));
    }

    public List<ProductCostDO> list(ProductCostQryParam param) {
        return this.pageListCost(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<ProductCostDO> pageListCost(Page<ProductCostDO> page, ProductCostQryParam param) {
        MPJLambdaWrapper<ProductCostDO> queryWrapper = JoinWrappers.lambda(ProductCostDO.class)
                .selectAll(ProductCostDO.class)
                .selectAs(ProductDO::getName, ProductCostDO::getProductName)
                .selectAs(ProductDO::getType, ProductCostDO::getProductType)
                .leftJoin(ProductDO.class, ProductDO::getNo, ProductCostDO::getProductNo)
                .eq(ObjectUtil.isNotEmpty(param.getProductNo()), ProductCostDO::getProductNo, param.getProductNo())
                .eq(ObjectUtil.isNotEmpty(param.getProductType()), ProductDO::getType, param.getProductType())
                .eq(ObjectUtil.isNotEmpty(param.getCostType()), ProductCostDO::getCostType, param.getCostType())
                .apply(CostVersion.CURRENT.equals(param.getVersion()), "t.id IN (SELECT MAX(id) AS id FROM data_product_cost GROUP BY product_no) ")
                .ge(ObjectUtil.isNotEmpty(param.getStart()), ProductCostDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), ProductCostDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(ProductCostDO::getProductNo, param.getSearchText()).or().like(ProductDO::getName, param.getSearchText()))
                .orderByDesc(ProductCostDO::getUpdateTime);

        return this.baseMapper.selectJoinPage(page, ProductCostDO.class, queryWrapper);
    }

    public List<ProductCostDO> listLate(Collection<?> productNoCol) {
        Wrapper<ProductCostDO> queryWrapper = Wrappers.lambdaQuery(ProductCostDO.class)
                .in(CollUtil.isNotEmpty(productNoCol), ProductCostDO::getProductNo, productNoCol)
                .apply("data_product_cost.id IN (SELECT MAX(id) AS id FROM data_product_cost GROUP BY product_no)");
        return this.list(queryWrapper);
    }

    public boolean adjust(ProductCostDO productCost) {
        ProductCostDO productCostDO = this.getById(productCost.getId());
        ProductCostDO productCostDO1 = BeanUtil.copyProperties(productCostDO, ProductCostDO.class, "id", "createTime", "createUser", "updateTime", "updateUser");
        
        productCostDO1.setCostQty(productCost.getCostQty());
        productCostDO1.setCostPrice(productCost.getCostPrice());
        productCostDO1.setCostDate(DateUtils.nowDate());
        productCostDO1.setCostType(CostType.MANUAL.name());
        productCostDO1.setRemark(String.format(Constant.COST_REMARK, CostType.MANUAL.getRemark(), ShiroUtils.getUser().getUsername()));
        return this.save(productCostDO1);
    }


}
