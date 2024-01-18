package com.bootdo.modular.cashier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.modular.cashier.domain.RecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (CashierRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-25 19:39:09
 */
public interface RecordDao extends BaseMapper<RecordDO> {

    Page<RecordDO> list(IPage<RecordDO> page, @Param("param") Map<String, Object> param);

    List<RecordDO> list(@Param("param") Map<String, Object> param);

    Map<String, Object> selectSum(@Param("param") Map<String, Object> map);

    List<RecordDO> multiSelect(@Param("param") Map<String, Object> map);
}

