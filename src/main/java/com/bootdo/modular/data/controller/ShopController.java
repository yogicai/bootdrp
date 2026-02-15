package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.DataShop;
import com.bootdo.modular.data.param.ShopQryParam;
import com.bootdo.modular.data.service.ShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺管理
 *
 * @author L
 * @since 2024-01-26 15:46
 */
@Tag(name = "店铺管理")
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
    public String edit(@PathVariable Integer id, Model model) {
        DataShop dataShop = shopService.getById(id);
        model.addAttribute("shop", dataShop);
        return "data/shop/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    public R<Void> save(DataShop dataShop) {
        shopService.add(dataShop);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/batchRemove")
    public R<Void> batchRemove(@RequestParam("ids[]") List<Long> ids) {
        shopService.removeByIds(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/selectManageShop")
    public R<List<DataShop>> selectPicker() {
        return R.ok(shopService.selectManageShop());
    }

}
