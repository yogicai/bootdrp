package com.bootdo.modular.system.controller;

import cn.hutool.json.JSONUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.system.domain.DictDO;
import com.bootdo.modular.system.service.DictService;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.R;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */
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
	public PageR list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<DictDO> sysDictList = sysDictService.list(query);
		int total = sysDictService.count(query);
		PageR pageR = new PageR(sysDictList, total);
		return pageR;
	}

	@GetMapping("/add")
	@RequiresPermissions("common:sysDict:add")
	String add() {
		return "system/dict/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("common:sysDict:edit")
	String edit(@PathVariable("id") Long id, Model model) {
		DictDO sysDict = sysDictService.get(id);
		model.addAttribute("sysDict", sysDict);
		return "system/dict/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("common:sysDict:add")
	public R save(DictDO sysDict) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (sysDictService.save(sysDict) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("common:sysDict:edit")
	public R update(DictDO sysDict) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		sysDictService.update(sysDict);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("common:sysDict:remove")
	public R remove(Long id) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (sysDictService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("common:sysDict:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		sysDictService.batchRemove(ids);
		return R.ok();
	}

	@GetMapping("/type")
	@ResponseBody
	public List<DictDO> listType() {
		return sysDictService.listType();
	};

	// 类别已经指定增加
	@GetMapping("/add/{type}/{description}")
	@RequiresPermissions("common:sysDict:add")
	String addD(Model model, @PathVariable("type") String type, @PathVariable("description") String description) {
		model.addAttribute("type", type);
		model.addAttribute("description", description);
		return "system/dict/add";
	}

	@ResponseBody
	@GetMapping("/list/{type}")
	public List<DictDO> listByType(@PathVariable("type") String type) {
		// 查询列表数据
		Map<String, Object> map = new HashMap<>(16);
		map.put("type", type);
		List<DictDO> dictList = sysDictService.list(map);
		return dictList;
	}

    /**
     * 下拉框数据（字典表）
     */
	@ResponseBody
	@GetMapping("/lists/{types}")
	public Map<String, List<DictDO>> listByTypes(@PathVariable("types") String types) {
		// 查询列表数据
		Map<String, Object> map = new HashMap<>(16);
		map.put("types", StringUtils.split(types, ","));
        Map<String, List<DictDO>> dictMaps = sysDictService.lists(map);
		return dictMaps;
	}

    /**
     * 业务数据下拉框数据
     */
    @ResponseBody
    @GetMapping("/listExtra")
    public Map<String, List<Map<String, String>>> listExtra() {
        // 查询列表数据
        Map<String, Object> map = ImmutableMap.of("status", 1);
        Map<String, List<Map<String, String>>> extraMaps = sysDictService.listExtra(map);
        return extraMaps;
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
