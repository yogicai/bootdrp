package com.bootdo.modular.engage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.engage.domain.ProductCostDO;

import java.util.List;
import java.util.Map;

/**
 * 商品成本表
 *
 * @author yogiCai
 * @date 2018-03-17 19:35:03
 */
public interface ProductCostDao extends BaseMapper<ProductCostDO> {

    ProductCostDO get(Integer id);

    List<ProductCostDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    List<ProductCostDO> listLate(Map<String, Object> map);

    List<ProductCostDO> costList(Map<String, Object> map);

    int costCount(Map<String, Object> map);

    int save(ProductCostDO productCost);

    int saveBatch(List<ProductCostDO> productCostDOList);

    int update(ProductCostDO productCost);

    int remove(Integer id);

    int batchRemove(Integer[] ids);
}
