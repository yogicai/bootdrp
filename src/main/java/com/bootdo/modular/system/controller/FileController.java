package com.bootdo.modular.system.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.config.properties.BootdoProperties;
import com.bootdo.core.enums.FileType;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.modular.system.param.SysFileParam;
import com.bootdo.modular.system.service.FileService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-19 16:02:20
 */
@Api(tags = "文件管理")
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController {
    @Resource
    private FileService sysFileService;
    @Resource
    private BootdoProperties bootdoProperties;

    @GetMapping()
    @RequiresPermissions("common:sysFile:sysFile")
    String sysFile(Model model) {
        return "system/file/file";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:sysFile:sysFile")
    public PageR list(SysFileParam param) {
        // 查询列表数据
        return sysFileService.page(param);
    }

    @GetMapping("/add")
    String add() {
        return "common/sysFile/add";
    }

    @GetMapping("/edit")
    String edit(Long id, Model model) {
        FileDO sysFile = sysFileService.getById(id);
        model.addAttribute("sysFile", sysFile);
        return "common/sysFile/edit";
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("common:info")
    public R info(@PathVariable("id") Long id) {
        FileDO sysFile = sysFileService.getById(id);
        return R.ok().put("sysFile", sysFile);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:save")
    public R save(FileDO sysFile) {
        sysFileService.save(sysFile);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("common:update")
    public R update(@RequestBody FileDO sysFile) {
        sysFileService.updateById(sysFile);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    public R remove(Long id) {
        sysFileService.removeFile(id);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:remove")
    public R remove(@RequestParam("ids[]") List<Integer> ids) {
        sysFileService.removeBatchByIds(ids);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/upload")
    R upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = StrUtil.replace(fileName, FileUtil.mainName(fileName), IdUtil.simpleUUID());

        FileDO sysFile = new FileDO(FileType.getFileType(fileName), "/files/" + fileName, new Date());
        FileUtil.writeBytes(file.getBytes(), FileUtil.file(bootdoProperties.getUploadPath(), fileName));

        sysFileService.save(sysFile);
        return R.ok().put("fileName", sysFile.getUrl());
    }


}
