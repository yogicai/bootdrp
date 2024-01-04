package com.bootdo.modular.system.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.config.properties.BootdoProperties;
import com.bootdo.modular.system.dao.DeptDao;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.dao.UserRoleDao;
import com.bootdo.modular.system.domain.DeptDO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.domain.UserRoleDO;
import com.bootdo.modular.system.service.UserService;
import com.bootdo.modular.system.controller.response.UserVO;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.modular.system.service.FileService;
import com.bootdo.core.utils.BuildTree;
import com.bootdo.core.enums.FileType;
import com.bootdo.core.utils.ImageUtils;
import com.bootdo.core.utils.MD5Utils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * @author L
 * @since 2023-12-15 21:23
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userMapper;
    @Resource
    UserRoleDao userRoleMapper;
    @Resource
    DeptDao deptMapper;
    @Resource
    private FileService sysFileService;
    @Resource
    private BootdoProperties bootdoProperties;

    @Override
    public UserDO get(Long id) {
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        UserDO user = userMapper.get(id);
        user.setDeptName(deptMapper.get(user.getDeptId()).getName());
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    public List<UserDO> list(Map<String, Object> map) {
        return userMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return userMapper.count(map);
    }

    @Transactional
    @Override
    public int save(UserDO user) {
        int count = userMapper.save(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (!list.isEmpty()) {
            userRoleMapper.batchSave(list);
        }
        return count;
    }

    @Override
    public int update(UserDO user) {
        int r = userMapper.update(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (!list.isEmpty()) {
            userRoleMapper.batchSave(list);
        }
        return r;
    }

    @Override
    public int remove(Long userId) {
        userRoleMapper.removeByUserId(userId);
        return userMapper.remove(userId);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        return !userMapper.list(params).isEmpty();
    }

    @Override
    public Set<String> listRoles(Long userId) {
        return null;
    }

    @Override
    public int resetPwd(UserVO userVO, UserDO userDO) throws Exception {
        if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
                userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
                return userMapper.update(userDO);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号！");
        }
    }

    @Override
    public int adminResetPwd(UserVO userVO) throws Exception {
        UserDO userDO = get(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
        return userMapper.update(userDO);
    }

    @Transactional
    @Override
    public int batchremove(Long[] userIds) {
        int count = userMapper.batchRemove(userIds);
        userRoleMapper.batchRemoveByUserId(userIds);
        return count;
    }

    @Override
    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
        List<DeptDO> depts = deptMapper.list(new HashMap<String, Object>(16));
        Long[] pDepts = deptMapper.listParentDept();
        Long[] uDepts = userMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
        for (DeptDO dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<UserDO> users = userMapper.list(new HashMap<String, Object>(16));
        for (UserDO user : users) {
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

    @Override
    public int updatePersonal(UserDO userDO) {
        return userMapper.update(userDO);
    }

    @Override
    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatarData, Long userId) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = StrUtil.replace(fileName, FileUtil.mainName(fileName), IdUtil.simpleUUID());

        FileDO sysFile = new FileDO(FileType.getFileType(fileName), "/files/" + fileName, new Date());
        //获取图片后缀
        String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
        String[] str = avatarData.split(",");
        //获取截取的x坐标
        int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
        //获取截取的y坐标
        int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
        //获取截取的高度
        int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
        //获取截取的宽度
        int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
        //获取旋转的角度
        int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
        try {
            BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
            BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(rotateImage, prefix, out);
            //转换后存入数据库
            byte[] b = out.toByteArray();

            cn.hutool.core.io.FileUtil.writeBytes(b, cn.hutool.core.io.FileUtil.file(bootdoProperties.getUploadPath() + fileName));
        } catch (Exception e) {
            throw new Exception("图片裁剪错误！！");
        }
        Map<String, Object> result = new HashMap<>();
        if (sysFileService.save(sysFile) > 0) {
            UserDO userDO = new UserDO();
            userDO.setUserId(userId);
            userDO.setPicId(sysFile.getId());
            if (userMapper.update(userDO) > 0) {
                result.put("url", sysFile.getUrl());
            }
        }
        return result;
    }


    public Map<String, List<Tree<Map>>> listTreeData(Map<String, Object> params) {
        List<Tree<Map>> trees = new ArrayList<>();
        List<Map> deptUserData = userMapper.listTreeData(params);

        Set<String> categorySet = Sets.newHashSet();
        for (Map map : deptUserData) {
            Tree<Map> tree = new Tree<>();
            if (categorySet.add(MapUtil.getStr(map, "deptId"))) {
                tree.setId(MapUtil.getStr(map, "deptId"));
                tree.setParentId("0");
                tree.setText(MapUtil.getStr(map, "name"));
                Map<String, Object> attributes = new HashMap<>(16);
                attributes.put("type", MapUtil.getStr(map, "type") + "_DATA");
                tree.setAttributes(attributes);
                trees.add(tree);
            }
            tree = new Tree<>();
            tree.setId(MapUtil.getStr(map, "dataId"));
            tree.setParentId(MapUtil.getStr(map, "deptId"));
            tree.setText(MapUtil.getStr(map, "dataName"));
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("type", MapUtil.getStr(map, "type") + "_DATA");
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Map>> treeList = BuildTree.buildList(trees, ImmutableSet.of("0"));
        Map<String, List<Tree<Map>>> listTree = Maps.newHashMap();
        for (Tree<Map> node : treeList) {
            String type = MapUtil.getStr(node.getAttributes(), "type");
            if (!listTree.containsKey(type)) {
                listTree.put(type, new ArrayList<>());
            }
            listTree.get(type).add(node);
        }
        return listTree;
    }
}
