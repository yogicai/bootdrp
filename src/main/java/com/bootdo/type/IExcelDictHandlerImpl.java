package com.bootdo.type;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.common.domain.DictDO;
import com.bootdo.common.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  * 支持字典参数设置
 *  * 举例： @Excel(name = "性别", width = 15, dicCode = "sex")
 *  * 1、导出的时候会根据字典配置，把值1,2翻译成：男、女;
 *  * 2、导入的时候，会把男、女翻译成1,2存进数据库;
 *
 * @author caiyz
 * @since 2022-07-08 22:50
 */
@Slf4j
@Component
public class IExcelDictHandlerImpl implements IExcelDictHandler {

    @Resource
    private DictService sysDictService;

    private Map<String, List<DictDO>> listDic = new HashMap<>();

    public static final String SERVICE_TYPE = "data_wh_ck_rk";

    @PostConstruct
    public void init() {
        listDic = sysDictService.lists(null);
        List<DictDO> serviceTypeDic = listDic.entrySet().stream().filter(entry -> StrUtil.equalsAny(entry.getKey(), "data_wh_ck", "data_wh_rk")).map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList());
        listDic.put(SERVICE_TYPE, serviceTypeDic);
    }

    /**
     * 从值翻译到名称
     *
     * @param dict  字典Key
     * @param obj   对象
     * @param name  属性名称
     * @param value 属性值
     */
    @Override
    public String toName(String dict, Object obj, String name, Object value) {
        if (!listDic.containsKey(dict)) {
            return StrUtil.toString(value);
        }
        DictDO dictDO = CollectionUtil.findOne(listDic.get(dict), (d) -> StrUtil.equalsAnyIgnoreCase(d.getValue(), StrUtil.toString(value)));
        return dictDO.getName();
    }

    /**
     * 从名称翻译到值
     *
     * @param dict  字典Key
     * @param obj   对象
     * @param name  属性名称
     * @param value 属性值
     */
    @Override
    public String toValue(String dict, Object obj, String name, Object value) {
        if (!listDic.containsKey(dict)) {
            return StrUtil.toString(value);
        }
        DictDO dictDO = CollectionUtil.findOne(listDic.get(dict), (d) -> StrUtil.equalsAnyIgnoreCase(d.getName(), StrUtil.toString(value)));
        return dictDO.getValue();
    }
}

