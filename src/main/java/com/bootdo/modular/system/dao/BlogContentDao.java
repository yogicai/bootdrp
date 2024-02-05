package com.bootdo.modular.system.dao;

import com.bootdo.modular.system.domain.ContentDO;

import java.util.List;
import java.util.Map;

/**
 * 文章内容
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 16:17:48
 */
public interface BlogContentDao {

    ContentDO get(Long cid);

    List<ContentDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ContentDO content);

    int update(ContentDO content);

    int remove(Long cid);

    int batchRemove(Long[] cids);
}
