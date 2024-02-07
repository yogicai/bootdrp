package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author L
 */
@TableName(value = "sys_role_menu")
@Data
public class RoleMenuDO {

    private Long id;
    private Long roleId;
    private Long menuId;

}
