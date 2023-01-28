package com.bootdo.report.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @date 2018-07-07 01:52:07
 */

@Mapper
public interface ReportDao {

    List<Map<String, Object>> sReconC(Map<String, Object> map);

    List<Map<String, Object>> sReconV(Map<String, Object> map);

    List<Map<String, Object>> mainTabCustomer(Map<String, Object> map);

    List<Map<String, Object>> mainTabProduct(Map<String, Object> map);

    List<Map<String, Object>> saleProduct(Map<String, Object> map);

}
