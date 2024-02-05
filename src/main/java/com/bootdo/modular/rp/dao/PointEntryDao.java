package com.bootdo.modular.rp.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 客户积分
 *
 * @author yogiCai
 * @since 2018-03-06 23:17:49
 */
public interface PointEntryDao extends MPJBaseMapper<PointEntryDO> {

    Page<PointEntryDO> listG(IPage<PointEntryDO> page, @Param("param") Map<String, Object> param);

}
