package com.bootdo.modular.system.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.config.properties.BootdoProperties;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.system.dao.FileDao;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.modular.system.param.SysFileParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author L
 */
@Service
public class FileService extends ServiceImpl<FileDao, FileDO> {
    @Resource
    private BootdoProperties bootdoProperties;

    public PageR page(SysFileParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<FileDO> list(SysFileParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<FileDO> pageList(Page<FileDO> page, SysFileParam param) {
        LambdaQueryWrapper<FileDO> queryWrapper = Wrappers.lambdaQuery(FileDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), FileDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), FileDO::getCreateDate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), FileDO::getCreateDate, param.getEnd());

        return this.page(page, queryWrapper);
    }

    public void removeFile(Long id) {
        FileDO fileDO = this.getById(id);
        this.removeById(id);
        FileUtil.del(bootdoProperties.getUploadPath() + fileDO.getUrl().replace("/files/", ""));
    }

    public Boolean isExist(String url) {
        boolean isExist = false;
        if (StrUtil.isNotBlank(url)) {
            String filePath = bootdoProperties.getUploadPath() + url.replace("/files/", "");
            return FileUtil.exist(filePath);
        }
        return isExist;
    }
}
