package com.bootdo.modular.system.service.impl;

import cn.hutool.core.io.IoUtil;
import com.bootdo.core.utils.GenUtils;
import com.bootdo.modular.system.dao.GeneratorDao;
import com.bootdo.modular.system.service.GeneratorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


/**
 * @author L
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Resource
    private GeneratorDao generatorDao;

    @Override
    public List<Map<String, Object>> list() {
        List<Map<String, Object>> list = generatorDao.list();
        return list;
    }

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = generatorDao.get(tableName);
            //查询列信息
            List<Map<String, String>> columns = generatorDao.listColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

}
