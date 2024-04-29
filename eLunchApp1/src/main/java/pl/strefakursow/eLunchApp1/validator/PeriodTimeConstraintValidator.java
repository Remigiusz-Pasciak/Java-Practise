package pl.strefakursow.eLunchApp1.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.strefakursow.eLunchApp1.model.Period;
import pl.strefakursow.eLunchApp1.model.PeriodTime;

public class PeriodTimeConstraintValidator implements ConstraintValidator<PeriodTimeConstraint, PeriodTime> {
    @Override
    public void initialize(PeriodTimeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(PeriodTime periodTime, ConstraintValidatorContext constraintValidatorContext) {

        try {
            return periodTime.getBegin() == null || periodTime.getEnd() == null || periodTime.getBegin().isBefore(periodTime.getEnd());

        } catch (Exception e){
            return false;
        }

    }
}
