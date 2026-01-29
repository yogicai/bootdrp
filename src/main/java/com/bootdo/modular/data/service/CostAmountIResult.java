package com.bootdo.modular.data.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.modular.engage.domain.ProductCostDO;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author L
 */
@Data
@Service
public class CostAmountIResult {

    private final Map<String, ProductCostDO> costMap = MapUtil.newHashMap();


}
