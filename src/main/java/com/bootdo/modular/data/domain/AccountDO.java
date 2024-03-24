package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import com.bootdo.core.pojo.base.param.BaseParam.edit;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@TableName(value = "data_account")
@Data
public class AccountDO extends BaseEntity {
    /**
     *
     */
    @NotNull(groups = {edit.class})
    private Integer id;
    /**
     * 编号
     */
    @NotNull(groups = {edit.class})
    private Integer no;
    /**
     * 帐户名称
     */
    private String name;
    /**
     * 帐户编码
     */
    private String code;
    /**
     * 帐户类型
     */
    private String type;
    /**
     * 期初日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 期初余额
     */
    private BigDecimal startBalance;
    /**
     * 当前余额
     */
    private BigDecimal currentBalance;
    /**
     * 状态
     */
    private Integer status = 0;
    /**
     * 店铺编号
     */
    @NotBlank
    private String shopNo;

}
