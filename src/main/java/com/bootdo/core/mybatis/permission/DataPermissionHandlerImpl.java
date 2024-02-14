package com.bootdo.core.mybatis.permission;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.bootdo.core.annotation.DataScope.DataType;
import com.bootdo.core.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 自定义数据权限处理
 *
 * @author L
 * @since 2024-01-29 20:23
 */
@Slf4j
@Component
public class DataPermissionHandlerImpl implements MultiDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {

        Collection<Long> shopNoList = ShiroUtils.getScopes(DataType.SHOP);

        if (ObjectUtil.isNotEmpty(shopNoList)) {
            // 把集合转变为JSQLParser需要的元素列表
            ItemsList ids = new ExpressionList(shopNoList.stream().map(LongValue::new).collect(Collectors.toList()));
            //in表达式的写法
            String columnName = ObjectUtil.defaultIfNull(table.getAlias(), Alias::getName, table.getName());

            return new InExpression(new Column(columnName + "." + DataType.SHOP.getColumn()), ids);
        }

        return null;
    }

}

