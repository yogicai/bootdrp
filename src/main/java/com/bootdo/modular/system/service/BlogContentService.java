package com.bootdo.modular.system.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.system.dao.BlogContentDao;
import com.bootdo.modular.system.domain.ContentDO;
import com.bootdo.modular.system.param.SysBlogParam;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author L
 */
@Service
public class BlogContentService extends ServiceImpl<BlogContentDao, ContentDO> {

    public PageR page(SysBlogParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<ContentDO> list(SysBlogParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<ContentDO> pageList(Page<ContentDO> page, SysBlogParam param) {
        LambdaQueryWrapper<ContentDO> queryWrapper = Wrappers.lambdaQuery(ContentDO.class)
                .eq(ObjectUtil.isNotEmpty(param.getCategories()), ContentDO::getCategories, param.getCategories())
                .ge(ObjectUtil.isNotEmpty(param.getStart()), ContentDO::getGtmModified, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), ContentDO::getGtmModified, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(ContentDO::getTitle, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

}
