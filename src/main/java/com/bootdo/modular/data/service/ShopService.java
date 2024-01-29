package com.bootdo.modular.data.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.data.dao.DataShopDao;
import com.bootdo.modular.data.domain.DataShop;
import com.bootdo.modular.data.param.ShopQryParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺表
 *
 * @author L
 * @since 2024-01-26 15:42
 */
@Service
public class ShopService extends ServiceImpl<DataShopDao, DataShop> {

    public PageR page(ShopQryParam param) {
        LambdaQueryWrapper<DataShop> queryWrapper = Wrappers.lambdaQuery(DataShop.class)
                .like(ObjectUtil.isNotEmpty(param.getName()), DataShop::getName, param.getName())
                .in(ObjectUtil.isNotNull(param.getStatus()), DataShop::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .and(ObjectUtil.isNotEmpty(param.getManagerId()), query -> StrUtil.split(param.getManagerId(), StrUtil.COMMA).forEach(id -> query.or().like(DataShop::getManagerId, id)))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), DataShop::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), DataShop::getUpdateTime, param.getEnd());

        Page<DataShop> page = this.page(PageFactory.defaultPage(), queryWrapper);

        return new PageR(page);
    }

    public void add(DataShop dataShop) {
        if (ObjectUtil.isNull(dataShop.getId())) {
            DataShop dataShopDo = this.getOne(Wrappers.<DataShop>query().select("max(no) as no"));
            dataShop.setNo(dataShopDo.getNo() + 1);
        }
        this.saveOrUpdate(dataShop);
    }

    public List<DataShop> selectPicker() {
        Wrapper<DataShop> queryWrapper = Wrappers.lambdaQuery(DataShop.class)
                .like(DataShop::getManagerId, ShiroUtils.getUserId() + StrUtil.COMMA);
        return this.list(queryWrapper);
    }

}




