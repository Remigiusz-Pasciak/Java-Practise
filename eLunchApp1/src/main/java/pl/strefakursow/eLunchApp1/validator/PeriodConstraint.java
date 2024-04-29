package pl.strefakursow.eLunchApp1.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodConstraintValidator.class)
@Documented
public @interface PeriodConstraint {

    String message() default "{pl.strefakursow.eLunchApp1.validator.PeriodConstraint}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
