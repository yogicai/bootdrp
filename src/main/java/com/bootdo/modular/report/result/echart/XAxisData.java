package com.bootdo.modular.report.result.echart;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.List;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class XAxisData {

    private String type;
    private List<String> data = CollUtil.newArrayList();

}
