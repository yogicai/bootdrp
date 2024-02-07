package com.bootdo.modular.system.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.system.dao.LogDao;
import com.bootdo.modular.system.domain.LogDO;
import com.bootdo.modular.system.param.SysLogParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author L
 */
@Service
public class LogService extends ServiceImpl<LogDao, LogDO> {

    public PageR page(SysLogParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<LogDO> list(SysLogParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<LogDO> pageList(Page<LogDO> page, SysLogParam param) {
        LambdaQueryWrapper<LogDO> queryWrapper = Wrappers.lambdaQuery(LogDO.class)
                .in(ObjectUtil.isNotEmpty(param.getUserId()), LogDO::getUserId, StrUtil.split(param.getUserId(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), LogDO::getGmtCreate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), LogDO::getGmtCreate, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(LogDO::getUsername, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

}
