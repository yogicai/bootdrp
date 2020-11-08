package com.bootdo.data.service;

import com.bootdo.data.dao.ProductCostDao;
import com.bootdo.data.dao.ProductDao;
import com.bootdo.data.domain.ProductCostDO;
import com.bootdo.data.domain.ProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
    @Autowired
    private ProductCostDao productCostDao;
	
	public ProductDO get(Integer id){
		return productDao.get(id);
	}
	
	public List<ProductDO> list(Map<String, Object> map){
		return productDao.list(map);
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
