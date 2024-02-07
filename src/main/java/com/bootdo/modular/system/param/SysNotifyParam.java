package com.bootdo.modular.system.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SysNotifyParam extends BaseParam {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "是否已读：0 未读、1 已读")
    private String isRead;

}
