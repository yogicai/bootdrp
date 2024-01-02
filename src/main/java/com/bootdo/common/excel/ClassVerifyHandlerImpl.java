package com.bootdo.common.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import org.springframework.stereotype.Component;

/**
 * excel行校验 去除空行
 *
 * @author caiyz
 * @since 2023-05-24 16:23
 */
@Component
public class ClassVerifyHandlerImpl implements IExcelVerifyHandler<Object> {

	public ExcelVerifyHandlerResult verifyHandler(Object obj) {

		ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);

		//判断对象属性是否全部为空
		BeanUtil.descForEach(obj.getClass(), action -> {

			if (ObjUtil.isNotEmpty(action.getValue(obj))) {
				result.setSuccess(true);
			}
		});
		return result;
	}

}
