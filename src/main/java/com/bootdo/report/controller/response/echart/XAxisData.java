package com.bootdo.report.controller.response.echart;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
@Data
public class XAxisData {

    private String type;
    private List<String> data = Lists.newArrayList();

}
