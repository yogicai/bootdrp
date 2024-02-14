package com.bootdo.modular.data.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.data.dao.ConsumerDao;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.param.ConsumerQryParam;
import org.springframework.stereotype.Service;


/**
 * @author L
 */
@Service
public class ConsumerService extends ServiceImpl<ConsumerDao, ConsumerDO> {

    public PageR page(ConsumerQryParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public PageJQ pageJQ(ConsumerQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<ConsumerDO> pageList(Page<ConsumerDO> page, ConsumerQryParam param) {
        LambdaQueryWrapper<ConsumerDO> queryWrapper = Wrappers.lambdaQuery(ConsumerDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), ConsumerDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), ConsumerDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), ConsumerDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), ConsumerDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(ConsumerDO::getNo, param.getSearchText()).or().like(ConsumerDO::getName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public ConsumerDO getByNo(String no) {
        return this.getOne(Wrappers.lambdaQuery(ConsumerDO.class).eq(ConsumerDO::getNo, no));
    }

}
