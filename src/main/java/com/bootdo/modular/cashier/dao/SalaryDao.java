package com.bootdo.modular.cashier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.cashier.domain.CashierSalary;
import com.bootdo.modular.cashier.result.JournalGeneralResult.SalaryRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工资表
 *
 * @author L
 */
public interface SalaryDao extends BaseMapper<CashierSalary> {

    /**
     * 工资年汇总
     */
    List<SalaryRecord> salaryRecordList(@Param("param") Map<String, Object> param);

}




