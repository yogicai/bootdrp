package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.service.ConsumerService;
import com.bootdo.modular.data.validator.DataValidator;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
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
@Api(tags = "客户管理")
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
    public PageR list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<ConsumerDO> consumerList = consumerService.list(query);
        int total = consumerService.count(query);
        return new PageR(consumerList, total);
    }

    @ResponseBody
    @GetMapping("/listJQ")
    @RequiresPermissions("data:consumer:consumer")
    public PageJQ listJQ(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<ConsumerDO> productList = consumerService.list(query);
        int total = consumerService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        return new PageJQ(productList, totalPage, query.getPage(), total);
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
