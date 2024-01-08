package com.bootdo.modular.report.result.echart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
* @author yogiCai
* @date 2018-02-01 10:43:43
*/
@Data
@AllArgsConstructor
public class PieData {

    private String name;
    private BigDecimal value;

}
