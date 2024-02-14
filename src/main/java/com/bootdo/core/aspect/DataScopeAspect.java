package com.bootdo.core.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.annotation.DataScope.DataType;
import com.bootdo.core.exception.BootServiceExceptionEnum;
import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.core.utils.ShiroUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * 数据权限切面
 *
 * @author L
 * @since 2024-02-07 20:03
 */
@Aspect
@Component
public class DataScopeAspect {

    @Around("@annotation(dataScope)")
    public Object doDataScope(ProceedingJoinPoint joinPoint, DataScope dataScope) throws Throwable {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            Object[] args = joinPoint.getArgs();
            BaseParam baseParam = (BaseParam) ArrayUtil.firstMatch(arg -> arg instanceof BaseParam, args);

            Arrays.stream(dataScope.type()).forEach(type -> {
                //店铺权限
                if (DataType.SHOP.equals(type)) {
                    Collection<Long> paramShopNoList = CollUtil.filterNew(baseParam.getShopNo(), ObjectUtil::isNotEmpty);
                    //1、baseParam未指定店铺，查所有有权限的店铺数据；2、baseParam指定了店铺，取交集
                    Collection<Long> shopNoList = CollUtil.isEmpty(paramShopNoList) ? ShiroUtils.getUser().getShopNos()
                            : CollUtil.intersectionDistinct(paramShopNoList, ShiroUtils.getUser().getShopNos());

                    BootServiceExceptionEnum.DATA_PERMISSION_NOT_VALID.assertNotEmpty(shopNoList, method.getName(), baseParam);
                    //设置ThreadLocal，DataPermissionHandlerImpl处理
                    ShiroUtils.setScopes(type, shopNoList);
                }
            });

            return joinPoint.proceed();

        } finally {
            ShiroUtils.SCOPE_DATA.remove();
        }
    }

}
