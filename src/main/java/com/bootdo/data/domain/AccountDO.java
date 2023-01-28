package com.bootdo.data.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
@Data
public class AccountDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /** 编号 */
    private Integer no;
    /** 帐户名称 */
    private String name;
    /** 帐户编码 */
    private String code;
    /** 帐户类型 */
    private String type;
    /** 期初日期 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 期初余额 */
    private BigDecimal startBalance;
    /** 当前余额 */
    private BigDecimal currentBalance;
    /** 状态 */
    private Integer status = 0;
    /** 创建时间 */
    private Date createTime;
    /** 更新余额 */
    private Date updateTime;

}
