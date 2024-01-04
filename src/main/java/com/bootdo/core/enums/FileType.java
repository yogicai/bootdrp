package com.bootdo.core.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;


/**
 * @author zss
 * 日期：2017年3月31日
 * 功能：根据文件名称判断类型
 * 接受参数类型：String
 * 返回参数类型：String
 * 备注：文件类型不完善，有需要的自行添加
 */

@AllArgsConstructor
@Getter
public enum FileType {

    /**
     * 图片
     */
    IMAGE(0, "图片", CollUtil.newHashSet("bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf")),
    /**
     * 文档
     */
    DOCUMENT(1, "文档", CollUtil.newHashSet("txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt")),
    /**
     * 视频
     */
    VIDEO(2, "视频", CollUtil.newHashSet("mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm")),
    /**
     * 音乐
     */
    MUSIC(3, "音乐", CollUtil.newHashSet("mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg", "m4a", "vqf")),
    ;

    private final Integer code;
    private final String remark;
    private final Set<String> typeColl;


    public static Integer getFileType(String fileName) {
        String extName = FileNameUtil.extName(fileName);
        for (FileType type : FileType.values()) {
            if (type.getTypeColl().contains(extName)) {
                return type.getCode();
            }
        }
        return 99;
    }
}