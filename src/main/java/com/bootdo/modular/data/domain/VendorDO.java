package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;


/**
 * 供应商信息表
 *
 * @author yogiCai
 * @since 2017-11-24 23:12:54
 */
@TableName(value = "data_vendor")
@Data
public class VendorDO extends BaseEntity {
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

}
