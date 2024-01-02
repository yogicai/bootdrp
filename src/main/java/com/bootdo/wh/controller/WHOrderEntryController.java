package com.bootdo.wh.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.R;
import com.bootdo.wh.controller.request.WHOrderVO;
import com.bootdo.wh.domain.WHOrderDO;
import com.bootdo.wh.service.WHOrderEntryService;
import com.bootdo.wh.validator.WHOrderValidator;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 入库出库商品
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */

@Controller
@RequestMapping("/wh/entry")
public class WHOrderEntryController {
    @Resource
    private WHOrderValidator whOrderValidator;
    @Resource
    private WHOrderEntryService whOrderEntryService;

    /**
     * 左侧菜单单据详情页URL
     */
    @GetMapping()
    @RequiresPermissions("wh:entry:entry")
    public String orderEntry(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "wh/entry/entry";
    }

    /**
     * 保存单据
     */
    @Log("库存单保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("se:entry:add")
    public R save(@RequestBody WHOrderVO order) {
        whOrderValidator.validateSave(order);
        WHOrderDO orderDO = whOrderEntryService.save(order);
        return R.ok(ImmutableMap.of("billNo", orderDO.getBillNo()));
    }

    /**
     * 保证单据页面，添加分录商品弹窗
     */
    @GetMapping("/add")
    @RequiresPermissions("wh:entry:add")
    public String add() {
        return "wh/entry/add";
    }

    /**
     * 保证单据页面，供应商弹窗
     */
    @GetMapping("/addHead")
    @RequiresPermissions("wh:entry:add")
    public String addHead() {
        return "wh/entry/addHead";
    }

    /**
     * 单据列表页双击，查询单据详情信息
     */
    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("wh:order:remove")
    public R get(@RequestParam Map<String, Object> params) {
        //查询列表数据
        WHOrderVO orderVO = whOrderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
