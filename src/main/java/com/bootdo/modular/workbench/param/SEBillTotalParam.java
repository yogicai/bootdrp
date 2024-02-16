package com.bootdo.modular.workbench.param;

import com.bootdo.core.enums.AuditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SEBillTotalParam {

    private String billDateStart;

    private AuditStatus auditStatus;

    private String shopNo;

}
