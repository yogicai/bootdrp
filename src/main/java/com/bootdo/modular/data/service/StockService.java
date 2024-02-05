package com.bootdo.modular.data.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.data.dao.StockDao;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.param.StockQryParam;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class StockService extends ServiceImpl<StockDao, StockDO> {

    public PageR page(StockQryParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<StockDO> pageList(Page<StockDO> page, StockQryParam param) {
        LambdaQueryWrapper<StockDO> queryWrapper = Wrappers.lambdaQuery(StockDO.class)
                .ge(ObjectUtil.isNotEmpty(param.getStatus()), StockDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), StockDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), StockDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(StockDO::getStockNo, param.getSearchText()).or().like(StockDO::getStockName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public Map<String, StockDO> listStock() {
        return this.pageList(PageFactory.defalultAllPage(), new StockQryParam())
                .getRecords()
                .stream()
                .collect(Collectors.toMap(StockDO::getStockNo, Function.identity(), (o, n) -> n));
    }

}
