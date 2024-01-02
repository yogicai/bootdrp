package com.bootdo.data.service;

import com.bootdo.engage.domain.ProductCostDO;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author L
 */
@Data
@Service
public class CostAmountIResult {

    private final Map<String, ProductCostDO> costMap = Maps.newHashMap();


}
