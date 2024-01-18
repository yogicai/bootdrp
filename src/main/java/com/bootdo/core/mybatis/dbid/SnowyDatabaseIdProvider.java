package com.bootdo.core.mybatis.dbid;

import com.bootdo.core.enums.DbIdEnum;
import org.apache.ibatis.mapping.DatabaseIdProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库id选择器
 *
 * @author xuyuxiang
 * @date 2019/3/30 22:26
 */
public class SnowyDatabaseIdProvider implements DatabaseIdProvider {

    @Override
    public void setProperties(Properties p) {
    }

    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
        String url = "";
        try (Connection connection = dataSource.getConnection()) {
            url = connection.getMetaData().getURL();
        }

        if (url.contains(DbIdEnum.ORACLE.getName())) {
            return DbIdEnum.ORACLE.getCode();
        } else if (url.contains(DbIdEnum.PG_SQL.getName())) {
            return DbIdEnum.PG_SQL.getCode();
        } else if (url.contains(DbIdEnum.MS_SQL.getName())) {
            return DbIdEnum.MS_SQL.getCode();
        } else if (url.contains(DbIdEnum.DM_SQL.getName())) {
            return DbIdEnum.DM_SQL.getCode();
        } else if (url.contains(DbIdEnum.KINGBASE_ES.getName())) {
            return DbIdEnum.KINGBASE_ES.getCode();
        } else {
            return DbIdEnum.MYSQL.getCode();
        }
    }
}
