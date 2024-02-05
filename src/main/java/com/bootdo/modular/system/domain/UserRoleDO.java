package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

/**
 * @author L
 */
@TableName(value = "sys_user_role")
@Data
public class UserRoleDO extends BaseEntity {
    private Long id;
    private Long userId;
    private Long roleId;
}
