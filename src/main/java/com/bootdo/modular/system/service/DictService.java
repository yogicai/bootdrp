package com.bootdo.modular.system.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.enums.CommonStatus;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.data.dao.StockDao;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.system.dao.DictDao;
import com.bootdo.modular.system.domain.DictDO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.param.SysDictParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class DictService extends ServiceImpl<DictDao, DictDO> {
    @Resource
    private StockDao stockDao;


    public PageR page(SysDictParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<DictDO> pageList(Page<DictDO> page, SysDictParam param) {
        LambdaQueryWrapper<DictDO> queryWrapper = Wrappers.lambdaQuery(DictDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), DictDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .eq(ObjectUtil.isNotEmpty(param.getValue()), DictDO::getValue, param.getValue())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(DictDO::getDescription, param.getSearchText()).or().like(DictDO::getName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public List<DictDO> listType() {
        LambdaQueryWrapper<DictDO> queryWrapper = Wrappers.lambdaQuery(DictDO.class).select(DictDO::getType, DictDO::getDescription);

        return this.list(queryWrapper).stream()
                .filter(PoiUtil.distinctByKey(dict -> StrUtil.concat(true, dict.getType(), dict.getDescription())))
                .collect(Collectors.toList());
    }

    public List<DictDO> getHobbyList(UserDO userDO) {

        List<String> userHobbyList = StrUtil.split(userDO.getHobby(), StrUtil.COMMA);

        SysDictParam sysDictParam = SysDictParam.builder().type("hobby").build();
        List<DictDO> hobbyList = this.pageList(PageFactory.defalultAllPage(), sysDictParam).getRecords();

        hobbyList.stream().filter(dictDO -> userHobbyList.contains(dictDO.getId().toString()))
                .forEach(dictDO -> dictDO.setRemarks("true"));

        return hobbyList;
    }

    public List<DictDO> getSexList() {
        SysDictParam sysDictParam = SysDictParam.builder().type("sex").build();
        return this.pageList(PageFactory.defalultAllPage(), sysDictParam).getRecords();
    }

    public Map<String, List<DictDO>> listMap(SysDictParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param)
                .getRecords()
                .stream()
                .collect(Collectors.groupingBy(DictDO::getType, Collectors.toList()));
    }

    public Map<String, List<Map<String, Object>>> listExtra() {
        List<StockDO> list = stockDao.selectList(Wrappers.lambdaQuery(StockDO.class).eq(StockDO::getStatus, CommonStatus.ENABLE.getValue()));

        List<Map<String, Object>> listMap = list.stream()
                .map(stock -> MapUtil.<String, Object>builder().put("name", stock.getStockName()).put("value", stock.getStockNo()).build())
                .collect(Collectors.toList());

        return MapUtil.of("data_stock", listMap);
    }
}
