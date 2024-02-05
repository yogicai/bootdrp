package com.bootdo.modular.report.dao;

import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @since 2018-07-07 01:52:07
 */

public interface ReportDao {

    List<Map<String, Object>> sReconC(Map<String, Object> map);

    List<Map<String, Object>> sReconV(Map<String, Object> map);

    List<Map<String, Object>> saleProduct(Map<String, Object> map);

}
