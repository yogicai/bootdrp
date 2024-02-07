package com.bootdo.modular.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 通知通告
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-05 17:11:16
 */
@TableName(value = "oa_notify")
@Data
public class NotifyDO {
    //编号
    private Long id;
    //类型
    private String type;
    //标题
    private String title;
    //内容
    private String content;
    //附件
    private String files;
    //状态
    private Integer status = 0;
    //创建者
    private Long createBy;
    //创建时间
    private Date createDate;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateDate;
    //备注信息
    private String remarks;
    //删除标记
    private Integer delFlag;

    @TableField(exist = false)
    private Long[] userIds;

}
