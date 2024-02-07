package com.bootdo.modular.system.dao;

import com.bootdo.modular.system.domain.MenuDO;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

/**
 * 菜单管理
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 09:45:09
 */
public interface MenuDao extends MPJBaseMapper<MenuDO> {

    List<MenuDO> listMenuByUserId(Long id);

    List<String> listUserPerms(Long id);
}
