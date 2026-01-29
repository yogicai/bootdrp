package com.bootdo.modular.report.result.echart;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.Data;

import java.util.List;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SeriesData {
    private String name;
    private String type;
    private int barGap;
    private List<Object> data = CollUtil.newArrayList();
    private MarkData markPoint = new MarkData();
    private MarkData markLine = new MarkData();


    @Data
    public class MarkData {
        private List<Object> data = CollUtil.newArrayList();

        public void addData(String name, String type) {
            this.data.add(MapUtil.<String, Object>builder()
                    .put("name", name)
                    .put("type", type)
                    .build());
        }
    }
}
