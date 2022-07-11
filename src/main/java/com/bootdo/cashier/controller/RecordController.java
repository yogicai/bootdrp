package com.bootdo.cashier.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.cashier.controller.response.MultiSelect;
import com.bootdo.cashier.domain.RecordDO;
import com.bootdo.cashier.service.RecordService;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.PageJQUtils;
import com.bootdo.common.utils.PoiUtil;
import com.bootdo.common.utils.QueryJQ;
import com.bootdo.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 日记账
 *
 * @author yogiCai
 * @date 2018-07-14 22:31:58
 */
 
@Controller
@RequestMapping("/cashier/record")
public class RecordController {
	@Resource
	private RecordService recordService;

	@GetMapping()
	@RequiresPermissions("cashier:record:record")
	String journal() {
		return "cashier/record/record";
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@RequiresPermissions("cashier:record:record")
	public PageJQUtils listP(@RequestBody Map<String, Object> params){
		return list(params);
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("cashier:record:record")
	public PageJQUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
		QueryJQ query = new QueryJQ(params);
		List<RecordDO> orderList = recordService.list(query);
		Map<String, Object> map = recordService.selectSum(query);
		int total = MapUtil.getInt(map, "totalCount", 0);
		int totalSum = MapUtil.getInt(map, "totalAmount", 0);
		int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
		PageJQUtils pageUtils = new PageJQUtils(orderList, totalPage, query.getPage(), total, totalSum);
		return pageUtils;
	}

	/**
	 * 导出
	 */
	@ResponseBody
	@GetMapping("/export")
	@RequiresPermissions("cashier:record:record")
	void sReconVCExport(@RequestParam Map<String, Object> params, Model model) {
		List<RecordDO> orderList = recordService.list(params);
		PoiUtil.exportExcelWithStream("TradeRecord.xls", RecordDO.class, orderList);
	}

	@GetMapping("/add")
	@RequiresPermissions("cashier:record:record")
	String add() {
		return "cashier/record/add";
	}


	@GetMapping("/edit/{id}")
	@RequiresPermissions("cashier:record:record")
	String edit(@PathVariable("id") Integer id, Model model) {
		RecordDO recordDO = recordService.get(id);
		model.addAttribute("record", recordDO);
		return "cashier/record/edit";
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("cashier:record:record")
	public R update(RecordDO recordDO) {
		recordService.update(recordDO);
		return R.ok();
	}

	/**
	 * 保存
	 */
	@Log("新增-修改日记账")
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("cashier:record:record")
	public R save(RecordDO recordDO) {
		if (recordService.save(recordDO.toManualRecord()) > 0) {
			return R.ok();
		}
		return R.error();
	}


	/**
	 * 删除
	 */
	@Log("删除日记账")
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("cashier:record:record")
	public R remove(@RequestParam("ids[]") Integer[] ids) {
		recordService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 导入日记账
	 */
	@Log("导入日记账")
	@PostMapping("/importCsv")
	@ResponseBody
	@RequiresPermissions("cashier:record:record")
	public R importCsv(@RequestPart("file") MultipartFile file) throws Exception {
		recordService.importCvs(file);
		return R.ok();
	}

	/**
	 * 多选搜索条件
	 */
	@GetMapping("/multiSelect")
	@ResponseBody
	@RequiresPermissions("cashier:record:record")
	public MultiSelect multiSelect() throws Exception {
		return recordService.multiSelect();
	}

}
