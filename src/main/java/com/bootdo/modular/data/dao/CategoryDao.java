package com.bootdo.modular.data.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.modular.data.domain.CategoryDO;
import com.bootdo.modular.data.result.CategoryDataResult;

import java.util.List;
import java.util.Map;

/**
 * 类别管理
 *
 * @author yogiCai
 * @since 2018-02-04 17:12:20
 */
@InterceptorIgnore
public interface CategoryDao extends BaseMapper<CategoryDO> {

    List<CategoryDataResult> listTreeData(Map<String, Object> map);
}
