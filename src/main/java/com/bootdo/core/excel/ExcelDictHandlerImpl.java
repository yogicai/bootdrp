package com.bootdo.core.excel;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.modular.system.domain.DictDO;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.modular.system.service.DictService;
import com.bootdo.modular.data.domain.CategoryDO;
import com.bootdo.modular.data.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  * 支持字典参数设置
 *  * 举例： @Excel(name = "性别", width = 15, dict = "sex")
 *  * 1、导出的时候会根据字典配置，把值1,2 翻译成：男、女;
 *  * 2、导入的时候，会把男、女翻译成1,2 存进数据库;
 *
 * @author caiyz
 * @since 2022-07-08 22:50
 */
@Slf4j
@Component
public class ExcelDictHandlerImpl implements IExcelDictHandler {
    @Resource
    private DictService sysDictService;
    @Resource
    private CategoryService categoryService;
    private Map<String, List<DictDO>> listDic = new HashMap<>();


    @PostConstruct
    public void init() {
        //字典
        listDic = sysDictService.lists(null);
        //类目管理
        Map<String, List<CategoryDO>> listCategory = categoryService.lists(null);
        listCategory.forEach((key, value) -> {
                    List<DictDO> dictDOList = value.stream().map(categoryDO -> {
                        DictDO dictDO = new DictDO();
                        dictDO.setType(categoryDO.getType());
                        dictDO.setName(categoryDO.getName());
                        dictDO.setValue(categoryDO.getCategoryId().toString());
                        return dictDO;
                    }).collect(Collectors.toList());

                    listDic.put(key, dictDOList);
                }
        );
        //枚举类型
        Map<String, List<Map<String, String>>> listEnum = EnumCollection.listEnumMap();
        listEnum.forEach((key, value) -> {
                    List<DictDO> dictDOList = value.stream().map(enumMap -> {
                        DictDO dictDO = new DictDO();
                        dictDO.setType(key);
                        dictDO.setName(MapUtil.getStr(enumMap, "name"));
                        dictDO.setValue(MapUtil.getStr(enumMap, "value"));
                        return dictDO;
                    }).collect(Collectors.toList());

                    listDic.put(key, dictDOList);
                }
        );
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
        String dictType = getDictType(dict);
        return CharSequenceUtil.isNotBlank(dictType) ? getDictName(dictType, value) : StrUtil.toString(value);

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
        String dictType = getDictType(dict);
        return CharSequenceUtil.isNotBlank(dictType) ? getDictValue(dictType, value) : StrUtil.toString(value);
    }

    /**
     * 获取字典key
     * @param dict 字典key，多个逗号分隔
     */
    private String getDictType (String dict) {
        List<String> dictTypes = CharSequenceUtil.split(dict, CharPool.COMMA);
        return CollUtil.findOne(dictTypes, type -> listDic.containsKey(type));
    }

    /**
     * 字典value转name
     * @param dictType 字典key
     * @param value 字段值
     */
    private String getDictName(String dictType, Object value) {
        DictDO dictDO = CollUtil.findOne(listDic.get(dictType), d -> CharSequenceUtil.equals(d.getValue(), StrUtil.toString(value)));
        return dictDO == null ? StrUtil.toString(value) : dictDO.getName();
    }

    /**
     * 字典name转value
     * @param dictType 字典key
     * @param value 字段值
     */
    private String getDictValue(String dictType, Object value) {
        DictDO dictDO = CollUtil.findOne(listDic.get(dictType), d -> CharSequenceUtil.equals(d.getName(), StrUtil.toString(value)));
        return dictDO == null ? StrUtil.toString(value) : dictDO.getValue();
    }

}
