package com.bootdo.engage.service;

import cn.hutool.core.bean.BeanUtil;
import com.bootdo.common.config.Constant;
import com.bootdo.common.enumeration.CostType;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.ShiroUtils;
import com.bootdo.engage.dao.ProductCostDao;
import com.bootdo.engage.domain.ProductCostDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author caiyz
 * @since 2022-03-2 14:10
 */
@Service
public class ProductCostService {

	@Resource
	private ProductCostDao productCostDao;


	public ProductCostDO get(Integer id){
		return productCostDao.get(id);
	}

	public List<ProductCostDO> costList(Map<String, Object> map){
		return productCostDao.costList(map);
	}

	public int costCount(Map<String, Object> map){
		return productCostDao.costCount(map);
	}

	public int adjust(ProductCostDO productCost){
		ProductCostDO productCostDO = productCostDao.get(productCost.getId());
		ProductCostDO productCostDO1 = BeanUtil.copyProperties(productCostDO, ProductCostDO.class, "id");

		productCostDO1.setCostQty(productCost.getCostQty());
		productCostDO1.setCostPrice(productCost.getCostPrice());
		productCostDO1.setCostDate(DateUtils.nowDate());
		productCostDO1.setCostType(CostType.MANUAL.name());
		productCostDO1.setRemark(String.format(Constant.COST_REMARK, CostType.MANUAL.remark(), ShiroUtils.getUser().getUsername()));
		return productCostDao.save(productCostDO1);
	}
	

}
