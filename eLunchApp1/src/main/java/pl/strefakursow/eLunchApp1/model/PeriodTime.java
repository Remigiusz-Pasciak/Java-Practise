package pl.strefakursow.eLunchApp1.model;

import jakarta.persistence.Embeddable;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import pl.strefakursow.eLunchApp1.validator.PeriodTimeConstraint;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@GeneratePojoBuilder
@PeriodTimeConstraint
@Embeddable
public class PeriodTime {

    @Nullable
    private LocalTime begin;

    @Nullable
    private LocalTime end;


    @Nullable
    public LocalTime getBegin() {
        return begin;
    }

    public void setBegin(@Nullable LocalTime begin) {
        this.begin = begin;
    }

    @Nullable
    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(@Nullable LocalTime end) {
        this.end = end;
    }
}
