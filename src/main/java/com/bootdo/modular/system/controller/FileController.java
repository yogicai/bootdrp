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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-19 16:02:20
 */
@Tag(name = "文件管理")
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController {
    @Resource
    private FileService sysFileService;
    @Resource
    private BootdoProperties bootdoProperties;

    @GetMapping()
    @PreAuthorize("hasAuthority('common:sysFile:sysFile')")
    String sysFile(Model model) {
        return "system/file/file";
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('common:sysFile:sysFile')")
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
    @PreAuthorize("hasAuthority('common:info')")
    public R info(@PathVariable Long id) {
        FileDO sysFile = sysFileService.getById(id);
        return R.ok().put("sysFile", sysFile);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('common:save')")
    public R save(FileDO sysFile) {
        sysFileService.save(sysFile);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('common:update')")
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
    @PreAuthorize("hasAuthority('common:remove')")
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
