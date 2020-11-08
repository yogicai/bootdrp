package com.bootdo.data.service;

import com.bootdo.data.domain.ProductCostDO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class CostAmountIResult {

	private final Map<String, ProductCostDO> costMap = Maps.newHashMap();


    public Map<String, ProductCostDO> getCostMap() {
        return costMap;
    }
}
