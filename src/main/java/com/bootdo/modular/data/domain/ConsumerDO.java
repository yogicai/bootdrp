package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;


/**
 * 客户信息表
 *
 * @author yogiCai
 * @since 2017-11-18 22:41:14
 */
@TableName(value = "data_consumer")
@Data
public class ConsumerDO extends BaseEntity {

    /**
     *
     */
    private Integer id;
    /**
     * 编号
     */
    private Integer no;
    /**
     * 名称
     */
    private String name;
    /**
     * 客户类别
     */
    private String type;
    /**
     * 客户等级
     */
    private String grade;
    /**
     * 电话
     */
    private String phone;
    /**
     * 地址
     */
    private String address;
    /**
     * 状态
     */
    private Integer status = 0;

}
