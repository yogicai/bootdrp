package com.bootdo.modular.system.domain;

import lombok.Data;

import java.util.Date;


/**
 * 文章内容
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-22 13:16:10
 */
@Data
public class ContentDO {
    //
    private Long cid;
    //标题
    private String title;
    //
    private String slug;
    //创建人id
    private Long created;
    //最近修改人id
    private Long modified;
    //内容
    private String content;
    //类型
    private String type;
    //标签
    private String tags;
    //分类
    private String categories;
    //
    private Integer hits;
    //评论数量
    private Integer commentsNum;
    //开启评论
    private Integer allowComment;
    //允许ping
    private Integer allowPing;
    //允许反馈
    private Integer allowFeed;
    //状态
    private Integer status;
    //作者
    private String author;
    //创建时间
    private Date gtmCreate;
    //修改时间
    private Date gtmModified;

}
