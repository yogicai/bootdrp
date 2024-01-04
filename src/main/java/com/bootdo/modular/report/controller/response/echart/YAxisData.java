package com.bootdo.modular.report.controller.response.echart;

import lombok.Data;

/**
* @author yogiCai
* @date 2018-02-01 10:43:43
*/
@Data
public class YAxisData {

    private String type = "value";
    private String name = "";
    private int min = 0;
    private int max = 250;
    private int interval = 50;

}
