package pl.strefakursow.eLunchApp1.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import java.util.List;

@GeneratePojoBuilder
@Entity
@DiscriminatorValue("deliverer")
public class Deliverer extends Employee {

    @Nullable
    @OneToMany(mappedBy = "deliverer")
    private List<Order> orderDTOS;


    @Nullable
    public List<Order> getOrders() {
        return orderDTOS;
    }

    public void setOrders(@Nullable List<Order> orderDTOS) {
        this.orderDTOS = orderDTOS;
    }
}
