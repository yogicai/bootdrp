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
import com.bootdo.modular.data.dao.VendorDao;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.data.param.VendorQryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author L
 */
@Service
public class VendorService extends ServiceImpl<VendorDao, VendorDO> {

    public PageR page(VendorQryParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public PageJQ pageJQ(VendorQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<VendorDO> pageList(Page<VendorDO> page, VendorQryParam param) {
        LambdaQueryWrapper<VendorDO> queryWrapper = Wrappers.lambdaQuery(VendorDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), VendorDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), VendorDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), VendorDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), VendorDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(VendorDO::getNo, param.getSearchText()).or().like(VendorDO::getName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public VendorDO getByNo(String no) {
        return this.getOne(Wrappers.lambdaQuery(VendorDO.class).eq(VendorDO::getNo, no));
    }

    @Transactional
    public void add(VendorDO vendor) {
        if (ObjectUtil.isNull(vendor.getId())) {
            VendorDO vendorDO = this.getOne(Wrappers.<VendorDO>query().select("max(no) as no"));
            vendor.setNo(vendorDO.getNo() + 1);
        }
        this.saveOrUpdate(vendor);
    }

}
