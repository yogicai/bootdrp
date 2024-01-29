package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.DataShop;
import com.bootdo.modular.data.param.ShopQryParam;
import com.bootdo.modular.data.service.ShopService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 店铺管理
 *
 * @author L
 * @since 2024-01-26 15:46
 */
@Api(tags = "店铺管理")
@Controller
@RequestMapping("/data/shop")
public class ShopController {
    @Resource
    private ShopService shopService;

    @GetMapping()
    public String index() {
        return "data/shop/shop";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageR list(ShopQryParam param) {
        return shopService.page(param);
    }

    @GetMapping("/add")
    public String add() {
        return "data/shop/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        DataShop dataShop = shopService.getById(id);
        model.addAttribute("shop", dataShop);
        return "data/shop/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(DataShop dataShop) {
        shopService.add(dataShop);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/batchRemove")
    public R remove(@RequestParam("ids[]") List<Long> ids) {
        return R.ok(shopService.removeByIds(ids));
    }

    @ResponseBody
    @GetMapping("/selectPicker")
    public R selectPicker() {
        return R.ok(shopService.selectPicker());
    }

}
