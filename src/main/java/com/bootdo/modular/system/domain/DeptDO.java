package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;


/**
 * 部门管理
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-27 14:28:36
 */
@TableName(value = "sys_dept")
@Data
public class DeptDO extends BaseEntity {
    //
    @TableId
    private Long deptId;
    //上级部门ID，一级部门为0
    private Long parentId;
    //部门名称
    private String name;
    //排序
    private Integer orderNum;
    //是否删除  -1：已删除  0：正常
    private Integer delFlag;

}
