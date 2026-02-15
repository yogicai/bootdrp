package com.bootdo.modular.system.controller;

import cn.hutool.json.JSONUtil;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.DictDO;
import com.bootdo.modular.system.param.SysDictParam;
import com.bootdo.modular.system.service.DictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-29 18:28:07
 */
@Tag(name = "数据字典")
@Controller
@RequestMapping("/common/sysDict")
public class DictController extends BaseController {
    @Resource
    private DictService sysDictService;

    @GetMapping()
    @PreAuthorize("hasAuthority('common:sysDict:sysDict')")
    String sysDict() {
        return "system/dict/sysDict";
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('common:sysDict:sysDict')")
    public PageR list(SysDictParam param) {
        return sysDictService.page(param);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('common:sysDict:add')")
    String add() {
        return "system/dict/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('common:sysDict:edit')")
    String edit(@PathVariable Long id, Model model) {
        DictDO sysDict = sysDictService.getById(id);
        model.addAttribute("sysDict", sysDict);
        return "system/dict/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('common:sysDict:add')")
    public R<Void> save(DictDO sysDict) {
        sysDictService.save(sysDict);
        return R.ok();
    }

    @ResponseBody
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('common:sysDict:edit')")
    public R<Void> update(DictDO sysDict) {
        sysDictService.updateById(sysDict);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('common:sysDict:remove')")
    public R<Void> remove(Long id) {
        sysDictService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @PreAuthorize("hasAuthority('common:sysDict:batchRemove')")
    public R<Void> batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        sysDictService.removeBatchByIds(ids);
        return R.ok();
    }

    @GetMapping("/type")
    @ResponseBody
    public List<DictDO> listType() {
        return sysDictService.listType();
    }

    @GetMapping("/addNew")
    @PreAuthorize("hasAuthority('common:sysDict:add')")
    public String addD(Model model, String type, String description) {
        model.addAttribute("type", type);
        model.addAttribute("description", description);
        return "system/dict/add";
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<DictDO> listByType(@PathVariable String type) {
        // 查询列表数据
        SysDictParam sysDictParam = SysDictParam.builder().type(type).build();
        return sysDictService.pageList(PageFactory.defalultAllPage(), sysDictParam).getRecords();
    }

    /**
     * 下拉框数据（字典表）
     */
    @ResponseBody
    @GetMapping("/lists/{types}")
    public Map<String, List<DictDO>> listByTypes(@PathVariable String types) {
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
    public Map<String, List<Map<String, String>>> listEnumMap(@PathVariable String types) {
        return EnumCollection.listEnumMap();
    }


    @ResponseBody
    @GetMapping("/listEnum/{types}")
    public String listEnum(@PathVariable String types) {
        return JSONUtil.toJsonStr(EnumCollection.listEnum());
    }

}
