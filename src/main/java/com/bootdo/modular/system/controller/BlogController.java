package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.modular.system.domain.ContentDO;
import com.bootdo.modular.system.param.SysBlogParam;
import com.bootdo.modular.system.service.BlogContentService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author L
 */
@Api(tags = "文章列表")
@RequestMapping("/blog")
@Controller
public class BlogController {
    @Resource
    private BlogContentService blogContentService;

    @GetMapping()
    String blog() {
        return "system/blog/index/main";
    }

    @ResponseBody
    @GetMapping("/open/list")
    public PageR openList(SysBlogParam param) {
        // 查询列表数据
        return blogContentService.page(param);
    }

    @GetMapping("/open/post/{cid}")
    String post(@PathVariable("cid") Long cid, Model model) {
        ContentDO bContentDO = blogContentService.getById(cid);
        model.addAttribute("bContent", bContentDO);
        model.addAttribute("gtmModified", DateUtils.format(bContentDO.getGtmModified()));
        return "system/blog/index/post";
    }

    @GetMapping("/open/page/{categories}")
    String about(@PathVariable("categories") String categories, Model model) {
        ContentDO bContentDO = blogContentService.list(SysBlogParam.builder().categories(categories).build()).get(0);
        model.addAttribute("bContent", bContentDO);
        return "system/blog/index/post";
    }
}
