package pl.strefakursow.eLunchApp1.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import pl.strefakursow.eLunchApp1.model.enums.Archive;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
public class RestaurantDTO {

    public static class View {
        public interface Id {}
        public interface Basic extends Id {}
        public interface Extended extends Basic {}
    }
    public interface DataUpdateValidation {}

    @JsonView(View.Id.class)
    @NotNull
    private UUID uuid;

    @JsonView(View.Basic.class)
    @NotBlank
    private String name;

    @JsonView(View.Basic.class)
    @NotNull
    @Embedded
    private LogginDataDTO logginDataDTO;

    @JsonView(View.Extended.class)
    @NotNull
    @Embedded
    private CompanyDataDTO companyDataDTO;

    @JsonView(View.Extended.class)
    @NotNull
    @Size(max = 7)
    private List<OpenTimeDTO> openTimeDTOS;

    @JsonView(View.Extended.class)
    @Nullable
    @Null(groups = DataUpdateValidation.class)
    private List<OrderDTO> orderDTOS;

    @JsonView(View.Extended.class)
    @Nullable
    @Null(groups = DataUpdateValidation.class)
    private List<MenuItemDTO> menuItemDTOS;

    @JsonIgnore
    @NotNull
    private List<DiscountCodeDTO> discountCodeDTOS;

    @JsonView(View.Extended.class)
    @NotNull
    private Archive archive;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LogginDataDTO getLogginData() {
        return logginDataDTO;
    }

    public void setLogginData(LogginDataDTO logginDataDTO) {
        this.logginDataDTO = logginDataDTO;
    }

    public CompanyDataDTO getCompanyData() {
        return companyDataDTO;
    }

    public void setCompanyData(CompanyDataDTO companyDataDTO) {
        this.companyDataDTO = companyDataDTO;
    }

    public List<OpenTimeDTO> getOpenTimeDTOS() {
        return openTimeDTOS;
    }

    public void setOpenTimes(List<OpenTimeDTO> openTimeDTOS) {
        this.openTimeDTOS = openTimeDTOS;
    }

    public List<OrderDTO> getOrders() {
        return orderDTOS;
    }

    public void setOrders(List<OrderDTO> orderDTOS) {
        this.orderDTOS = orderDTOS;
    }

    public List<MenuItemDTO> getMenuItemDTOS() {
        return menuItemDTOS;
    }

    public void setMenuItems(List<MenuItemDTO> menuItemDTOS) {
        this.menuItemDTOS = menuItemDTOS;
    }

    public List<DiscountCodeDTO> getDiscountCodes() {
        return discountCodeDTOS;
    }

    public void setDiscountCodes(List<DiscountCodeDTO> discountCodeDTOS) {
        this.discountCodeDTOS = discountCodeDTOS;
    }

    public Archive getArchive() {
        return archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }
}
