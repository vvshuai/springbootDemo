package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 15:46 2020/5/1
 * @Modified By:
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    public ValidationResult validate(Object bean){
        final ValidationResult validationResult= new ValidationResult();
        Set<ConstraintViolation<Object> > constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size()>0){
            validationResult.setHadErrors(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                validationResult.getErrMasMap().put(propertyName, errMsg);
            });
        }
        return validationResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
