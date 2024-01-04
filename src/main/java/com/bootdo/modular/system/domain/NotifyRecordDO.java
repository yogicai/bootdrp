package com.bootdo.modular.system.domain;

import lombok.Data;

import java.util.Date;


/**
 * 通知通告发送记录
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-10 11:08:06
 */
@Data
public class NotifyRecordDO {
    /**
     *  编号
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
