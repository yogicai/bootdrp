package com.bootdo.report.controller.response.echart;

import java.math.BigDecimal;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class PieData {

    private String name;
    private BigDecimal value;

    public PieData(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
