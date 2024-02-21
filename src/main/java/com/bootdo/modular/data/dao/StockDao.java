package com.bootdo.modular.data.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.data.domain.StockDO;

/**
 * 仓库表
 *
 * @author yogiCai
 * @since 2018-02-18 16:23:32
 */
@InterceptorIgnore
public interface StockDao extends BaseMapper<StockDO> {

}
