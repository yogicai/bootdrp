package com.bootdo.modular.system.result;

import com.bootdo.modular.system.domain.UserDO;
import lombok.Data;

/**
 * @author gaoyuzhe
 * @since 2017/12/15.
 */
@Data
public class UserVO {
    /**
     * 更新的用户对象
     */
    private UserDO userDO = new UserDO();
    /**
     * 旧密码
     */
    private String pwdOld;
    /**
     * 新密码
     */
    private String pwdNew;

}
