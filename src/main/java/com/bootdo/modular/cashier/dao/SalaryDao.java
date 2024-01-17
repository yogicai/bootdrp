package com.bootdo.modular.cashier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.cashier.domain.CashierSalary;
import com.bootdo.modular.cashier.result.JournalGeneralResult.SalaryRecord;

import java.util.List;

/**
 * 工资表
 * @author L
 */
public interface SalaryDao extends BaseMapper<CashierSalary> {

    /**
     * 工资年汇总
     */
    List<SalaryRecord> salaryRecordList();

}




