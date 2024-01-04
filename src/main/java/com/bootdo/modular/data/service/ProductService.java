package com.bootdo.modular.data.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.modular.data.dao.ProductDao;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.engage.dao.ProductCostDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import org.springframework.stereotype.Service;

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
public class ProductService {
	@Resource
	private ProductDao productDao;
	@Resource
	private ProductCostDao productCostDao;
	
	public ProductDO get(Integer id){
		return productDao.get(id);
	}

	public List<ProductDO> list(Map<String, Object> map) {
		//商品列表
		List<ProductDO> productDOList = productDao.list(map);
		//商品成本
		Set<Integer> productNoSet = productDOList.stream().map(ProductDO::getNo).collect(Collectors.toSet());
		Map<String, Object> param = MapUtil.<String, Object>builder().put("latest", true).put("productNos", productNoSet).build();

		Map<String, ProductCostDO> productCostDoMap = productCostDao.listLate(param).stream()
				.collect(Collectors.toMap(ProductCostDO::getProductNo, v -> v, (o, n) -> n));

		productDOList.forEach(productDo -> {
			String productNo = productDo.getNo().toString();
			if (productCostDoMap.containsKey(productNo)) {
				productDo.setCostPrice(productCostDoMap.get(productNo).getCostPrice());
				productDo.setCostQty(productCostDoMap.get(productNo).getCostQty());
			} else {
				productDo.setCostPrice(productDo.getPurchasePrice());
				productDo.setCostQty(BigDecimal.ZERO);
			}
		});
		return productDOList;
	}
	
	public int count(Map<String, Object> map){
		return productDao.count(map);
	}
	
	public int save(ProductDO product){
		return productDao.save(product);
	}
	
	public int update(ProductDO product){
		return productDao.update(product);
	}
	
	public int remove(Integer id){
		return productDao.remove(id);
	}
	
	public int batchRemove(Integer[] ids){
		return productDao.batchRemove(ids);
	}

	public List<ProductCostDO> listCost(Map<String, Object> map){
        return productCostDao.list(map);
    }

	public int countCost(Map<String, Object> map){
		return productCostDao.count(map);
	}
}
