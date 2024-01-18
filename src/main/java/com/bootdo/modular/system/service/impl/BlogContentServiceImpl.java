package com.bootdo.modular.system.service.impl;

import com.bootdo.modular.system.dao.BlogContentDao;
import com.bootdo.modular.system.domain.ContentDO;
import com.bootdo.modular.system.service.BlogContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author L
 */
@Service
public class BlogContentServiceImpl implements BlogContentService {
    @Resource
    private BlogContentDao blogContentMapper;

    @Override
    public ContentDO get(Long cid) {
        return blogContentMapper.get(cid);
    }

    @Override
    public List<ContentDO> list(Map<String, Object> map) {
        return blogContentMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return blogContentMapper.count(map);
    }

    @Override
    public int save(ContentDO bContent) {
        return blogContentMapper.save(bContent);
    }

    @Override
    public int update(ContentDO bContent) {
        return blogContentMapper.update(bContent);
    }

    @Override
    public int remove(Long cid) {
        return blogContentMapper.remove(cid);
    }

    @Override
    public int batchRemove(Long[] cids) {
        return blogContentMapper.batchRemove(cids);
    }

}
