package com.bootdo.core.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.bootdo.core.excel.enums.VerifyResultEnum;
import com.bootdo.core.excel.param.BaseExcelParam;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * excel行校验 去除空行
 *
 * @author L
 * @since 2023-03-29 10:30
 */
@Component
public class ClassExcelVerifyHandler implements IExcelVerifyHandler<Object> {

    private static final Set<String> MODEL_FIELD = CollUtil.newHashSet("rowNum", "errorMsg");

    public ExcelVerifyHandlerResult verifyHandler(Object obj) {

        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);

        //判断对象属性是否全部为空
        BeanUtil.descForEach(obj.getClass(), action -> {

            if (ObjUtil.isNotEmpty(action.getValue(obj)) && !MODEL_FIELD.contains(action.getFieldName())) {
                result.setSuccess(true);
            }
        });

        //空行
        if (obj instanceof BaseExcelParam && !result.isSuccess()) {
            ((BaseExcelParam) obj).setVerifyResultEnum(VerifyResultEnum.ROW_NULL);
        }

        return result;
    }

}
