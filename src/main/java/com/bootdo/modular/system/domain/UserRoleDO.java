package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author L
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_user_role")
@Data
public class UserRoleDO {
    private Long id;
    private Long userId;
    private Long roleId;
}
