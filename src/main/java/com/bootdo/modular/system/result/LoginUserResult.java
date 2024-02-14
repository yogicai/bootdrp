package com.bootdo.modular.system.result;

import lombok.Data;

/**
 * 登录用户信息
 *
 * @author L
 * @since 2024-02-05 14:48
 */
@Data
public class LoginUserResult {

    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String name;
    /**
     * 账户名
     */
    private String username;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 店铺编号
     */
    private String shopNo;

}
