package com.bootdo.modular.data.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 仓库表
 *
 * @author yogiCai
 * @date 2018-02-18 16:23:32
 */
@Data
public class StockDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /** 仓库编号 */
    private String stockNo;
    /** 仓库名称 */
    private String stockName;
    /** 仓库地址 */
    private String stockAddress;
    /** 状态 */
    private Integer status = 0;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;

}
