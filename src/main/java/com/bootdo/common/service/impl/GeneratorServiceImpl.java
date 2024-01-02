package com.bootdo.common.service.impl;

import cn.hutool.core.io.IoUtil;
import com.bootdo.common.dao.GeneratorMapper;
import com.bootdo.common.service.GeneratorService;
import com.bootdo.common.utils.GenUtils;
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
	GeneratorMapper generatorMapper;

	@Override
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list = generatorMapper.list();
		return list;
	}

	@Override
	public byte[] generatorCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = generatorMapper.get(tableName);
			//查询列信息
			List<Map<String, String>> columns = generatorMapper.listColumns(tableName);
			//生成代码
			GenUtils.generatorCode(table, columns, zip);
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

}
