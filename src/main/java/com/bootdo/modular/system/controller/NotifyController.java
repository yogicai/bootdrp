package com.bootdo.modular.system.controller;

import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.NotifyDO;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.bootdo.modular.system.param.SysNotifyParam;
import com.bootdo.modular.system.service.NotifyRecordService;
import com.bootdo.modular.system.service.NotifyService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 通知通告
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-05 17:11:16
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
    public PageR list(SysNotifyParam param) {
        // 查询列表数据
        return notifyService.page(param);
    }

    @GetMapping("/add")
    @RequiresPermissions("oa:notify:add")
    String add() {
        return "system/notify/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("oa:notify:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        NotifyDO notify = notifyService.getById(id);
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
        notify.setCreateBy(getUserId());
        notifyService.saveNotify(notify);
        return R.ok();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("oa:notify:edit")
    public R update(NotifyDO notify) {
        notifyService.updateById(notify);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("oa:notify:remove")
    public R remove(Long id) {
        notifyService.removeNotify(id);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("oa:notify:batchRemove")
    public R remove(@RequestParam("ids[]") List<Long> ids) {
        notifyService.batchRemoveNotify(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/message")
    PageR message() {
        return notifyService.selfList(SysNotifyParam.builder().userId(getUserId()).isRead(Constant.OA_NOTIFY_READ_NO).build());
    }

    @GetMapping("/selfNotify")
    String selfNotify() {
        return "system/notify/selfNotify";
    }

    @ResponseBody
    @GetMapping("/selfList")
    PageR selfList() {
        return notifyService.selfList(SysNotifyParam.builder().userId(getUserId()).build());
    }

    @GetMapping("/read/{id}")
    @RequiresPermissions("oa:notify:edit")
    String read(@PathVariable("id") Long id, Model model) {
        NotifyDO notify = notifyService.getById(id);
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
