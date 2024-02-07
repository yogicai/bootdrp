package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author L
 */
@TableName(value = "sys_role")
@Data
public class RoleDO {

    @TableId
    private Long roleId;
    private String roleName;
    private String roleSign;
    private String remark;
    private Long userIdCreate;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    @TableField(exist = false)
    private List<Long> menuIds;

}
