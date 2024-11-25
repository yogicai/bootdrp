package com.bootdo.core.validation;

import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author L
 * @since 2024-10-14 16:58
 */
public class ELValidator implements ConstraintValidator<EL, Object> {

    //private final BeanExpressionContextAccessor beanExpressionContextAccessor = new BeanExpressionContextAccessor();
    private final ExpressionParser parser = new SpelExpressionParser();

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || !BeanUtil.isBean(value.getClass())) {
            return true;
        }

        StandardEvaluationContext contextEvaluator = new StandardEvaluationContext();
        //contextEvaluator.addPropertyAccessor(beanExpressionContextAccessor);
        contextEvaluator.setRootObject(value);

        AtomicReference<Integer> result = new AtomicReference<>(0);

        BeanUtil.descForEach(value.getClass(), action -> {
            if (action.getField().isAnnotationPresent(EL.class)) {
                EL el = action.getField().getAnnotation(EL.class);
                Boolean elValue = parser.parseExpression(el.expression()).getValue(contextEvaluator, Boolean.class);
                if (Boolean.FALSE.equals(elValue)) {
                    addViolation(context, el.message(), action.getFieldName());
                    result.getAndUpdate(x -> x + 1);
                }
            }
        });

        return result.get() == 0;
    }

    private void addViolation(ConstraintValidatorContext context, String messageTemplate, String fieldName) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(fieldName)
                .addBeanNode()
                .addConstraintViolation();
    }

}
