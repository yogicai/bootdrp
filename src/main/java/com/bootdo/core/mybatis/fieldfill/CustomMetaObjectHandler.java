package com.bootdo.core.mybatis.fieldfill;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.system.domain.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.util.Date;

/**
 * 自定义sql字段填充器，自动填充创建修改相关字段
 *
 * @author xuyuxiang
 * @date 2020/3/30 15:21
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_USER = "createUser";

    private static final String CREATE_TIME = "createTime";

    private static final String UPDATE_USER = "updateUser";

    private static final String UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Date date = new Date();

            //为空则设置createUser（BaseEntity)
            Object createUser = metaObject.getValue(CREATE_USER);
            if(ObjectUtil.isNull(createUser)) {
                setFieldValByName(CREATE_USER, this.getUserUniqueId(), metaObject);
            }

            //为空则设置createTime（BaseEntity)
            Object createTime = metaObject.getValue(CREATE_TIME);
            if(ObjectUtil.isNull(createTime)) {
                setFieldValByName(CREATE_TIME, date, metaObject);
            }

            //为空则设置updateUser（BaseEntity)
            Object updateUser = metaObject.getValue(UPDATE_USER);
            if(ObjectUtil.isNull(updateUser)) {
                setFieldValByName(UPDATE_USER, this.getUserUniqueId(), metaObject);
            }

            //为空则设置updateTime（BaseEntity)
            Object updateTime = metaObject.getValue(UPDATE_TIME);
            if(ObjectUtil.isNull(updateTime)) {
                setFieldValByName(UPDATE_TIME, date, metaObject);
            }
        } catch (ReflectionException e) {
            log.warn(">>> CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            //设置updateUser（BaseEntity)
            setFieldValByName(UPDATE_USER, this.getUserUniqueId(), metaObject);
            //设置updateTime（BaseEntity)
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        } catch (ReflectionException e) {
            log.warn(">>> CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }
    }

    /**
     * 获取用户唯一id
     */
    private Long getUserUniqueId() {
        try {
            UserDO sysLoginUser = ShiroUtils.getUser();
            if(ObjectUtil.isNotNull(sysLoginUser)) {
                return sysLoginUser.getUserId();
            } else {
                return -1L;
            }
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }
    }
}
