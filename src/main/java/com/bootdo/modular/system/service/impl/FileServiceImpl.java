package com.bootdo.modular.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bootdo.config.properties.BootdoProperties;
import com.bootdo.modular.system.dao.FileDao;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.modular.system.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * @author L
 */
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileDao sysFileMapper;
    @Resource
    private BootdoProperties bootdoProperties;

    @Override
    public FileDO get(Long id) {
        return sysFileMapper.get(id);
    }

    @Override
    public List<FileDO> list(Map<String, Object> map) {
        return sysFileMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return sysFileMapper.count(map);
    }

    @Override
    public int save(FileDO sysFile) {
        return sysFileMapper.save(sysFile);
    }

    @Override
    public int update(FileDO sysFile) {
        return sysFileMapper.update(sysFile);
    }

    @Override
    public int remove(Long id) {
        return sysFileMapper.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return sysFileMapper.batchRemove(ids);
    }

    @Override
    public Boolean isExist(String url) {
        boolean isExist = false;
        if (!StrUtil.isBlank(url)) {
            String filePath = url.replace("/files/", "");
            filePath = bootdoProperties.getUploadPath() + filePath;
            File file = new File(filePath);
            if (file.exists()) {
                isExist = true;
            }
        }
        return isExist;
    }
}
