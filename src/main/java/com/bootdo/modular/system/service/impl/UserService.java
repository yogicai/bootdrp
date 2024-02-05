package com.bootdo.modular.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.config.properties.BootdoProperties;
import com.bootdo.core.enums.FileType;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.BuildTree;
import com.bootdo.core.utils.ImageUtils;
import com.bootdo.core.utils.MD5Utils;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.dao.UserRoleDao;
import com.bootdo.modular.system.domain.DeptDO;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.domain.UserRoleDO;
import com.bootdo.modular.system.param.SysUserParam;
import com.bootdo.modular.system.result.LoginUserResult;
import com.bootdo.modular.system.result.UserVO;
import com.bootdo.modular.system.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author L
 * @since 2023-12-15 21:23
 */
@Transactional
@Service
public class UserService extends ServiceImpl<UserDao, UserDO> {
    @Resource
    private UserRoleDao userRoleMapper;
    @Resource
    private DeptService deptService;
    @Resource
    private FileService sysFileService;
    @Resource
    private BootdoProperties bootdoProperties;


    public PageR page(SysUserParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<UserDO> pageList(Page<UserDO> page, SysUserParam param) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .in(ObjectUtil.isNotEmpty(param.getName()), UserDO::getName, StrUtil.split(param.getName(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getUserName()), UserDO::getUsername, StrUtil.split(param.getUserName(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getDeptId()), UserDO::getDeptId, StrUtil.split(param.getDeptId(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), UserDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), UserDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(UserDO::getName, param.getSearchText()).or().like(UserDO::getUsername, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public UserDO getUser(Long id) {
        UserDO user = this.getById(id);
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        user.setDeptName(deptService.getById(user.getDeptId()).getName());
        user.setRoleIds(roleIds);
        return user;
    }

    @Transactional
    public boolean save(UserDO user) {
        this.saveOrUpdate(user);
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
        return true;
    }

    public void update(UserDO user) {
        this.updateById(user);
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
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeUser(Long userId) {
        userRoleMapper.removeByUserId(userId);
        this.removeById(userId);
    }

    public boolean exit(String userName) {
        return this.count(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, userName)) > 0;
    }

    public void resetPwd(UserVO userVO, UserDO userDO) throws Exception {
        if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
                userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
                this.updateById(userDO);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号！");
        }
    }

    public void adminResetPwd(UserVO userVO) throws Exception {
        UserDO userDO = getUser(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
        this.updateById(userDO);
    }

    @Transactional
    public void batchRemove(List<Integer> userIds) {
        this.removeByIds(userIds);
        userRoleMapper.batchRemoveByUserId(userIds);
    }

    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = new ArrayList<>();
        List<DeptDO> deptAll = deptService.list();
        Set<Long> pDept = deptAll.stream().map(DeptDO::getParentId).collect(Collectors.toSet());
        Set<Long> uDept = this.list().stream().map(UserDO::getDeptId).collect(Collectors.toSet());

        Collection<Long> allDeptIdSet = CollUtil.union(pDept, uDept);
        for (DeptDO dept : deptAll) {
            if (!allDeptIdSet.contains(dept.getDeptId())) {
                continue;
            }
            Tree<DeptDO> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            tree.setState(MapUtil.<String, Object>builder().put("opened", true).put("mType", "dept").build());
            trees.add(tree);
        }
        List<UserDO> users = this.list();
        for (UserDO user : users) {
            Tree<DeptDO> tree = new Tree<>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            tree.setState(MapUtil.<String, Object>builder().put("opened", true).put("mType", "user").build());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

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
            if (this.updateById(userDO)) {
                result.put("url", sysFile.getUrl());
            }
        }
        return result;
    }

    public LoginUserResult loginUserInfo() {
        UserDO userDO = this.getUser(ShiroUtils.getUserId());
        return BeanUtil.copyProperties(userDO, LoginUserResult.class);
    }

}
