package com.bootdo.data.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.*;
import com.bootdo.data.domain.ConsumerDO;
import com.bootdo.data.service.ConsumerService;
import com.bootdo.data.validator.DataValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 客户信息表
 *
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/data/consumer")
public class ConsumerController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private ConsumerService consumerService;

    @GetMapping()
    @RequiresPermissions("data:consumer:consumer")
    public String consumer() {
        return "data/consumer/consumer";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("data:consumer:consumer")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<ConsumerDO> consumerList = consumerService.list(query);
        int total = consumerService.count(query);
        return new PageUtils(consumerList, total);
    }

    @ResponseBody
    @GetMapping("/listJQ")
    @RequiresPermissions("data:consumer:consumer")
    public PageJQUtils listJQ(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<ConsumerDO> productList = consumerService.list(query);
        int total = consumerService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        return new PageJQUtils(productList, totalPage, query.getPage(), total);
    }

    @GetMapping("/add")
    @RequiresPermissions("data:consumer:add")
    public String add() {
        return "data/consumer/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("data:consumer:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        ConsumerDO consumer = consumerService.get(id);
        model.addAttribute("consumer", consumer);
        return "data/consumer/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("data:consumer:add")
    public R save(ConsumerDO consumer) {
        dataValidator.validateConsumer(consumer);
        if (consumerService.save(consumer) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("data:consumer:edit")
    public R update(ConsumerDO consumer) {
        dataValidator.validateConsumer(consumer);
        consumerService.update(consumer);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("data:consumer:remove")
    public R remove(Integer id) {
        if (consumerService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("data:consumer:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        consumerService.batchRemove(ids);
        return R.ok();
    }

}
