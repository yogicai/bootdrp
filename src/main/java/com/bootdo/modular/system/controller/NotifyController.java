package com.bootdo.modular.system.controller;

import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.NotifyDO;
import com.bootdo.modular.system.domain.NotifyRecordDO;
import com.bootdo.modular.system.param.SysNotifyParam;
import com.bootdo.modular.system.service.NotifyRecordService;
import com.bootdo.modular.system.service.NotifyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 通知通告
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-05 17:11:16
 */
@Tag(name = "通知公告")
@Controller
@RequestMapping("/oa/notify")
public class NotifyController extends BaseController {
    @Resource
    private NotifyService notifyService;
    @Resource
    private NotifyRecordService notifyRecordService;

    @GetMapping()
    @PreAuthorize("hasAuthority('oa:notify:notify')")
    public String oaNotify() {
        return "system/notify/notify";
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('oa:notify:notify')")
    public PageR list(SysNotifyParam param) {
        // 查询列表数据
        return notifyService.page(param);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('oa:notify:add')")
    public String add() {
        return "system/notify/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('oa:notify:edit')")
    public String edit(@PathVariable Long id, Model model) {
        NotifyDO notify = notifyService.getById(id);
        model.addAttribute("notify", notify);
        return "system/notify/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('oa:notify:add')")
    public R<Void> save(NotifyDO notify) {
        notify.setCreateBy(getUserId());
        notifyService.saveNotify(notify);
        return R.ok();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('oa:notify:edit')")
    public R<Void> update(NotifyDO notify) {
        notifyService.updateById(notify);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('oa:notify:remove')")
    public R<Void> remove(Long id) {
        notifyService.removeNotify(id);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @PreAuthorize("hasAuthority('oa:notify:batchRemove')")
    public R<Void> batchRemove(@RequestParam("ids[]") List<Long> ids) {
        notifyService.batchRemoveNotify(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/message")
    public PageR message() {
        return notifyService.selfList(SysNotifyParam.builder().userId(getUserId()).isRead(Constant.OA_NOTIFY_READ_NO).build());
    }

    @GetMapping("/selfNotify")
    public String selfNotify() {
        return "system/notify/selfNotify";
    }

    @ResponseBody
    @GetMapping("/selfList")
    public PageR selfList() {
        return notifyService.selfList(SysNotifyParam.builder().userId(getUserId()).build());
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAuthority('oa:notify:edit')")
    public String read(@PathVariable Long id, Model model) {
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
