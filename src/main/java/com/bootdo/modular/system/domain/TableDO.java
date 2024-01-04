package com.bootdo.modular.system.domain;

import lombok.Data;

import java.util.List;

/**
 * 表数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月20日 上午12:02:55
 */
@Data
public class TableDO {
    //表的名称
    private String tableName;
    //表的备注
    private String comments;
    //表的主键
    private ColumnDO pk;
    //表的列名(不包含主键)
    private List<ColumnDO> columns;

    //类名(第一个字母大写)，如：sys_user => SysUser
    private String className;
    //类名(第一个字母小写)，如：sys_user => sysUser
    private String classCamelName;

}
