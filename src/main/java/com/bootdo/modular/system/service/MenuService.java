package com.bootdo.modular.system.service;

import java.util.List;
import java.util.Set;

import com.bootdo.modular.system.domain.MenuDO;
import org.springframework.stereotype.Service;

import com.bootdo.core.pojo.node.Tree;

@Service
public interface MenuService {
	Tree<MenuDO> getSysMenuTree(Long id);

	List<Tree<MenuDO>> listMenuTree(Long id);

	Tree<MenuDO> getTree();

	Tree<MenuDO> getTree(Long id);

	List<MenuDO> list();

	int remove(Long id);

	int save(MenuDO menu);

	int update(MenuDO menu);

	MenuDO get(Long id);

	Set<String> listPerms(Long userId);
}
