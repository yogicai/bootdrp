package com.bootdo.modular.data.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.param.AccountQryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author L
 */
@Service
public class AccountService extends ServiceImpl<AccountDao, AccountDO> {

    public PageR page(AccountQryParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<AccountDO> pageList(Page<AccountDO> page, AccountQryParam param) {
        LambdaQueryWrapper<AccountDO> queryWrapper = Wrappers.lambdaQuery(AccountDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), AccountDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), AccountDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), AccountDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(AccountDO::getNo, param.getSearchText()).or().like(AccountDO::getName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    @Transactional
    public void add(AccountDO account) {
        if (ObjectUtil.isNull(account.getId())) {
            AccountDO accountDO = this.getOne(Wrappers.<AccountDO>query().select("max(no) as no"));
            account.setNo(accountDO.getNo() + 1);
        }
        this.saveOrUpdate(account);
    }

}
