package com.bootdo.modular.system.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.ContentDO;
import com.bootdo.modular.system.param.SysBlogParam;
import com.bootdo.modular.system.service.BlogContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 文章内容
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-09 10:03:34
 */
@Tag(name = "发布文章")
@Controller
@RequestMapping("/blog/bContent")
public class BlogContentController extends BaseController {
    @Resource
    private BlogContentService bBlogContentService;

    @GetMapping()
    @PreAuthorize("hasAuthority('blog:bContent:bContent')")
    public String bContent() {
        return "system/blog/bContent/bContent";
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('blog:bContent:bContent')")
    public PageR list(SysBlogParam param) {
        return bBlogContentService.page(param);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('blog:bContent:add')")
    public String add() {
        return "system/blog/bContent/add";
    }

    @GetMapping("/edit/{cid}")
    @PreAuthorize("hasAuthority('blog:bContent:edit')")
    public String edit(@PathVariable Long cid, Model model) {
        ContentDO bContentDO = bBlogContentService.getById(cid);
        model.addAttribute("bContent", bContentDO);
        return "system/blog/bContent/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('blog:bContent:add')")
    @PostMapping("/save")
    public R<Long> save(ContentDO bContent) {
        bContent.setAllowComment(ObjectUtil.defaultIfNull(bContent.getAllowComment(), 0));
        bContent.setAllowFeed(ObjectUtil.defaultIfNull(bContent.getAllowFeed(), 0));
        bContent.setType(ObjectUtil.defaultIfNull(bContent.getType(), "article"));
        bContent.setGtmCreate(new Date());
        bContent.setGtmModified(new Date());
        bBlogContentService.saveOrUpdate(bContent);
        return R.ok(bContent.getCid());
    }

    /**
     * 修改
     */
    @PreAuthorize("hasAuthority('blog:bContent:edit')")
    @ResponseBody
    @RequestMapping("/update")
    public R<Void> update(ContentDO bContent) {
        bContent.setGtmCreate(new Date());
        bBlogContentService.updateById(bContent);
        return R.ok();
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAuthority('blog:bContent:remove')")
    @PostMapping("/remove")
    @ResponseBody
    public R<Void> remove(Long id) {
        bBlogContentService.removeById(id);
        return R.ok();
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAuthority('blog:bContent:batchRemove')")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R<Void> batchRemove(@RequestParam("ids[]") List<Integer> cids) {
        bBlogContentService.removeBatchByIds(cids);
        return R.ok();
    }
}
