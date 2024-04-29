package pl.strefakursow.eLunchApp1.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.strefakursow.eLunchApp1.model.Period;

public class PeriodConstraintValidator implements ConstraintValidator<PeriodConstraint, Period> {
    @Override
    public void initialize(PeriodConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Period period, ConstraintValidatorContext constraintValidatorContext) {

        try {
            return period.getBegin() == null || period.getEnd() == null || period.getBegin().isBefore(period.getEnd());

        } catch (Exception e){
            return false;
        }

    }
}
