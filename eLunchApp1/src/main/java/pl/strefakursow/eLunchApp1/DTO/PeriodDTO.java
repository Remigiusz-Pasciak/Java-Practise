package pl.strefakursow.eLunchApp1.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Embeddable;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import pl.strefakursow.eLunchApp1.validator.PeriodConstraint;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@GeneratePojoBuilder
@PeriodConstraint
@Embeddable
public class PeriodDTO {
    public static class View {
        public interface Basic {}
    }

    @JsonView(View.Basic.class)
    @Nullable
    private LocalDateTime begin;

    @JsonView(View.Basic.class)
    @Nullable
    private LocalDateTime end;


    @Nullable
    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(@Nullable LocalDateTime begin) {
        this.begin = begin;
    }

    @Nullable
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(@Nullable LocalDateTime end) {
        this.end = end;
    }
}
