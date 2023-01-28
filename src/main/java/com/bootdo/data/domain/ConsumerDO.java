package com.bootdo.data.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 客户信息表
 *
 * @author yogiCai
 * @date 2017-11-18 22:41:14
 */
@Data
public class ConsumerDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /** 编号 */
    private Integer no;
    /** 名称 */
    private String name;
    /** 客户类别 */
    private String type;
    /** 客户等级 */
    private String grade;
    /** 电话 */
    private String phone;
    /** 地址 */
    private String address;
    /** 状态 */
    private Integer status = 0;
    /**  */
    private Date createTime;
    /**  */
    private Date updateTime;

}
