package com.bootdo.modular.data.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.data.dao.ProductDao;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.data.param.ProductQryParam;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.service.ProductCostService;
import com.bootdo.modular.system.dao.DictDao;
import com.bootdo.modular.system.domain.DictDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class ProductService extends ServiceImpl<ProductDao, ProductDO> {
    @Resource
    private DictDao dictDao;
    @Resource
    private ProductCostService productCostService;

    public PageR page(ProductQryParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public PageJQ pageJQ(ProductQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<ProductDO> selectList(ProductQryParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<ProductDO> pageList(Page<ProductDO> page, ProductQryParam param) {
        LambdaQueryWrapper<ProductDO> queryWrapper = Wrappers.lambdaQuery(ProductDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), ProductDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), ProductDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), ProductDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), ProductDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(ProductDO::getNo, param.getSearchText()).or().like(ProductDO::getName, param.getSearchText()));
        Page<ProductDO> pageList = this.page(page, queryWrapper);
        //商品成本
        Set<Integer> productNoSet = pageList.getRecords().stream().map(ProductDO::getNo).collect(Collectors.toSet());
        Map<String, ProductCostDO> productCostDoMap = productCostService.listLate(productNoSet).stream()
                .collect(Collectors.toMap(ProductCostDO::getProductNo, v -> v, (o, n) -> n));
        //商品单位
        Map<String, DictDO> unitMap = dictDao.selectList(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getType, "data_unit"))
                .stream().collect(Collectors.toMap(DictDO::getValue, v -> v, (o, n) -> n));

        pageList.getRecords().forEach(productDo -> {
            String productNo = productDo.getNo().toString();
            if (productCostDoMap.containsKey(productNo)) {
                productDo.setCostPrice(productCostDoMap.get(productNo).getCostPrice());
                productDo.setCostQty(productCostDoMap.get(productNo).getCostQty());
            } else {
                productDo.setCostPrice(productDo.getPurchasePrice());
                productDo.setCostQty(BigDecimal.ZERO);
            }
            productDo.setUnitName(ObjectUtil.defaultIfNull(unitMap.get(productDo.getUnit()), DictDO::getName, productDo.getUnit()));
        });

        return pageList;
    }

    @Transactional
    public void add(ProductDO product) {
        if (ObjectUtil.isNull(product.getId())) {
            ProductDO productDO = this.getOne(Wrappers.<ProductDO>query().select("max(no) as no"));
            product.setNo(productDO.getNo() + 1);
        }
        this.saveOrUpdate(product);
    }

}
