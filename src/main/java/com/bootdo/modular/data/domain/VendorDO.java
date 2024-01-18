package com.bootdo.modular.data.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 供应商信息表
 *
 * @author yogiCai
 * @date 2017-11-24 23:12:54
 */
@Data
public class VendorDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 编号
     */
    private Integer no;
    /**
     * 供应商名称
     */
    private String name;
    /**
     * 供应商类别
     */
    private String type;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系人电话
     */
    private String phone;
    /**
     * 供应商地址
     */
    private String address;
    /**
     * 状态
     */
    private Integer status = 0;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}
