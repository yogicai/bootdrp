package com.bootdo.modular.data.dao;

import com.bootdo.modular.data.domain.CategoryDO;

import java.util.List;
import java.util.Map;

/**
 * 类别管理
 *
 * @author yogiCai
 * @date 2018-02-04 17:12:20
 */
public interface CategoryDao {

    CategoryDO get(Long categoryId);

    List<CategoryDO> list(Map<String, Object> map);

    List<Map<String, Object>> listTreeData(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(CategoryDO category);

    int update(CategoryDO category);

    int remove(Long category_id);

    int batchRemove(Long[] categoryIds);
}
