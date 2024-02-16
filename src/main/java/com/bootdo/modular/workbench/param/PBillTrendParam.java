package com.bootdo.modular.workbench.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 订单趋势饼图
 *
 * @author L
 * @since 2024-02-16 17:52
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PBillTrendParam {

    private String type;

    private String shopNo;

}
