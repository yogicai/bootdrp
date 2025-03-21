package com.bootdo.modular.cashier.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.cashier.enums.DateTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 收款对账
 *
 * @author L
 * @since 2025-03-19 14:50
 */
@Data
public class ReconcileParam extends BaseParam {

    @NotNull
    private DateTypeEnum dateType;

}
