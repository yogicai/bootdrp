package com.bootdo.modular.system.controller;

import cn.hutool.json.JSONUtil;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.DictDO;
import com.bootdo.modular.system.param.SysDictParam;
import com.bootdo.modular.system.service.DictService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-29 18:28:07
 */
@Api(tags = "数据字典")
@Controller
@RequestMapping("/common/sysDict")
public class DictController extends BaseController {
    @Resource
    private DictService sysDictService;

    @GetMapping()
    @RequiresPermissions("common:sysDict:sysDict")
    String sysDict() {
        return "system/dict/sysDict";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:sysDict:sysDict")
    public PageR list(SysDictParam param) {
        // 查询列表数据
        return sysDictService.page(param);
    }

    @GetMapping("/add")
    @RequiresPermissions("common:sysDict:add")
    String add() {
        return "system/dict/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:sysDict:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        DictDO sysDict = sysDictService.getById(id);
        model.addAttribute("sysDict", sysDict);
        return "system/dict/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:sysDict:add")
    public R save(DictDO sysDict) {
        sysDictService.save(sysDict);
        return R.ok();
    }

    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("common:sysDict:edit")
    public R update(DictDO sysDict) {
        sysDictService.updateById(sysDict);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:sysDict:remove")
    public R remove(Long id) {
        sysDictService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:sysDict:batchRemove")
    public R remove(@RequestParam("ids[]") List<Integer> ids) {
        sysDictService.removeBatchByIds(ids);
        return R.ok();
    }

    @GetMapping("/type")
    @ResponseBody
    public List<DictDO> listType() {
        return sysDictService.listType();
    }

    @GetMapping("/addNew")
    @RequiresPermissions("common:sysDict:add")
    String addD(Model model, String type, String description) {
        model.addAttribute("type", type);
        model.addAttribute("description", description);
        return "system/dict/add";
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<DictDO> listByType(@PathVariable("type") String type) {
        // 查询列表数据
        SysDictParam sysDictParam = SysDictParam.builder().type(type).build();
        return sysDictService.pageList(PageFactory.defalultAllPage(), sysDictParam).getRecords();
    }

    /**
     * 下拉框数据（字典表）
     */
    @ResponseBody
    @GetMapping("/lists/{types}")
    public Map<String, List<DictDO>> listByTypes(@PathVariable("types") String types) {
        // 查询列表数据
        return sysDictService.listMap(SysDictParam.builder().type(types).build());
    }

    /**
     * 业务数据下拉框数据
     */
    @ResponseBody
    @GetMapping("/listExtra")
    public Map<String, List<Map<String, Object>>> listExtra() {
        // 查询列表数据
        return sysDictService.listExtra();
    }

    /**
     * 下拉框数据（枚举类）
     */
    @ResponseBody
    @GetMapping("/listEnumMap/{types}")
    public Map<String, List<Map<String, String>>> listEnumMap(@PathVariable("types") String types) {
        return EnumCollection.listEnumMap();
    }


    @ResponseBody
    @GetMapping("/listEnum/{types}")
    public String listEnum(@PathVariable("types") String types) {
        return JSONUtil.toJsonStr(EnumCollection.listEnum());
    }

}
