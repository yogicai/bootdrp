package com.bootdo.modular.system.controller;

import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.ContentDO;
import com.bootdo.modular.system.service.BlogContentService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章内容
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-09 10:03:34
 */
@Api(tags = "发布文章")
@Controller
@RequestMapping("/blog/bContent")
public class BlogContentController extends BaseController {
    @Resource
    BlogContentService bBlogContentService;

    @GetMapping()
    @RequiresPermissions("blog:bContent:bContent")
    String bContent() {
        return "system/blog/bContent/bContent";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("blog:bContent:bContent")
    public PageR list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<ContentDO> bContentList = bBlogContentService.list(query);
        int total = bBlogContentService.count(query);
        PageR pageR = new PageR(bContentList, total);
        return pageR;
    }

    @GetMapping("/add")
    @RequiresPermissions("blog:bContent:add")
    String add() {
        return "system/blog/bContent/add";
    }

    @GetMapping("/edit/{cid}")
    @RequiresPermissions("blog:bContent:edit")
    String edit(@PathVariable("cid") Long cid, Model model) {
        ContentDO bContentDO = bBlogContentService.get(cid);
        model.addAttribute("bContent", bContentDO);
        return "system/blog/bContent/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions("blog:bContent:add")
    @PostMapping("/save")
    public R save(ContentDO bContent) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (bContent.getAllowComment() == null) {
            bContent.setAllowComment(0);
        }
        if (bContent.getAllowFeed() == null) {
            bContent.setAllowFeed(0);
        }
        if (null == bContent.getType()) {
            bContent.setType("article");
        }
        bContent.setGtmCreate(new Date());
        bContent.setGtmModified(new Date());
        int count;
        if (bContent.getCid() == null || "".equals(bContent.getCid())) {
            count = bBlogContentService.save(bContent);
        } else {
            count = bBlogContentService.update(bContent);
        }
        if (count > 0) {
            return R.ok().put("cid", bContent.getCid());
        }
        return R.error();
    }

    /**
     * 修改
     */
    @RequiresPermissions("blog:bContent:edit")
    @ResponseBody
    @RequestMapping("/update")
    public R update(ContentDO bContent) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        bContent.setGtmCreate(new Date());
        bBlogContentService.update(bContent);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequiresPermissions("blog:bContent:remove")
    @PostMapping("/remove")
    @ResponseBody
    public R remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (bBlogContentService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @RequiresPermissions("blog:bContent:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") Long[] cids) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        bBlogContentService.batchRemove(cids);
        return R.ok();
    }
}
