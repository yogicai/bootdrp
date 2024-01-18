package com.bootdo.modular.system.controller;

import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.NotifyDO;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.bootdo.modular.system.service.NotifyRecordService;
import com.bootdo.modular.system.service.NotifyService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知通告
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-05 17:11:16
 */
@Api(tags = "通知公告")
@Controller
@RequestMapping("/oa/notify")
public class NotifyController extends BaseController {
    @Resource
    private NotifyService notifyService;
    @Resource
    private NotifyRecordService notifyRecordService;

    @GetMapping()
    @RequiresPermissions("oa:notify:notify")
    String oaNotify() {
        return "system/notify/notify";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("oa:notify:notify")
    public PageR list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<NotifyDO> notifyList = notifyService.list(query);
        int total = notifyService.count(query);
        PageR pageR = new PageR(notifyList, total);
        return pageR;
    }

    @GetMapping("/add")
    @RequiresPermissions("oa:notify:add")
    String add() {
        return "system/notify/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("oa:notify:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        NotifyDO notify = notifyService.get(id);
        model.addAttribute("notify", notify);
        return "system/notify/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("oa:notify:add")
    public R save(NotifyDO notify) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        notify.setCreateBy(getUserId());
        if (notifyService.save(notify) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("oa:notify:edit")
    public R update(NotifyDO notify) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        notifyService.update(notify);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("oa:notify:remove")
    public R remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (notifyService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("oa:notify:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        notifyService.batchRemove(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/message")
    PageR message() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("offset", 0);
        params.put("limit", 3);
        Query query = new Query(params);
        query.put("userId", getUserId());
        query.put("isRead", Constant.OA_NOTIFY_READ_NO);
        return notifyService.selfList(query);
    }

    @GetMapping("/selfNotify")
    String selefNotify() {
        return "system/notify/selfNotify";
    }

    @ResponseBody
    @GetMapping("/selfList")
    PageR selfList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        query.put("userId", getUserId());

        return notifyService.selfList(query);
    }

    @GetMapping("/read/{id}")
    @RequiresPermissions("oa:notify:edit")
    String read(@PathVariable("id") Long id, Model model) {
        NotifyDO notify = notifyService.get(id);
        //更改阅读状态
        NotifyRecordDO notifyRecordDO = new NotifyRecordDO();
        notifyRecordDO.setNotifyId(id);
        notifyRecordDO.setUserId(getUserId());
        notifyRecordDO.setReadDate(new Date());
        notifyRecordDO.setIsRead(Constant.OA_NOTIFY_READ_YES);
        notifyRecordService.changeRead(notifyRecordDO);
        model.addAttribute("notify", notify);
        return "system/notify/read";
    }
}
