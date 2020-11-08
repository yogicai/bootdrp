package com.bootdo.report.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
@Mapper
public interface WHReportDao {

	List<Map<String, Object>> pBalance(Map<String, Object> map);

}
