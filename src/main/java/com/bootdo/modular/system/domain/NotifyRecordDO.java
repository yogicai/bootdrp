package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 通知通告发送记录
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-10 11:08:06
 */
@TableName(value = "oa_notify_record")
@Data
public class NotifyRecordDO {
    /**
     * 编号
     */
    private Long id;
    //通知通告ID
    private Long notifyId;
    //接受人
    private Long userId;
    //阅读标记
    private Integer isRead;
    //阅读时间
    private Date readDate;

}
