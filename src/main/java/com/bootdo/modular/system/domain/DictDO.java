package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-29 18:28:07
 */
@TableName(value = "sys_dict")
@Data
public class DictDO extends BaseEntity {
    //编号
    private Long id;
    //标签名
    private String name;
    //数据值
    private String value;
    //类型
    private String type;
    //描述
    private String description;
    //排序（升序）
    private BigDecimal sort;
    //父级编号
    private Long parentId;
    //创建者
    private Integer createBy;
    //创建时间
    private Date createDate;
    //更新者
    private Long updateBy;
    //更新时间
    private Date updateDate;
    //备注信息
    private String remarks;
    //删除标记
    private String delFlag;

}
