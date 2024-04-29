package pl.strefakursow.eLunchApp1.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import pl.strefakursow.eLunchApp1.model.enums.Archive;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


@GeneratePojoBuilder
public class UserDTO {

    public static class View {
        public interface Id {}
        public interface Basic extends Id {}
        public interface Extended extends Basic {}
    }
    public interface DataUpdateValidation {}
    public interface NewOperationValidation {}

    @JsonView(View.Id.class)
    @NotNull
    private UUID uuid;

    @JsonView(View.Basic.class)
    @NotNull
    @Embedded
    private PersonalDataDTO personalData;

    @JsonView(View.Extended.class)
    @Nullable
    private List<DeliveryAddressDTO> deliveryAddress;

    @JsonView(View.Extended.class)
    @NotNull
    @Embedded
    private LogginDataDTO logginData;

    @JsonIgnore
    @Nullable
    @Null(groups = DataUpdateValidation.class)
    private List<OrderDTO> orders;

    @JsonView(View.Extended.class)
    @NotNull
    @Size(max = 0, groups = DataUpdateValidation.class)
    @Size(min = 1, max = 1, groups = NewOperationValidation.class)
    private List<OperationEvidenceDTO> operationEvidence;

    @JsonView(View.Extended.class)
    @Nullable
    private List<DiscountCodeDTO> discountCodes;

    @JsonView(View.Extended.class)
    @NotNull
    private Archive archive;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public PersonalDataDTO getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDataDTO personalData) {
        this.personalData = personalData;
    }

    @Nullable
    public List<DeliveryAddressDTO> getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(@Nullable List<DeliveryAddressDTO> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LogginDataDTO getLogginData() {
        return logginData;
    }

    public void setLogginData(LogginDataDTO logginData) {
        this.logginData = logginData;
    }

    @Nullable
    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(@Nullable List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<OperationEvidenceDTO> getOperationEvidence() {
        return operationEvidence;
    }

    public void setOperationEvidence(List<OperationEvidenceDTO> operationEvidence) {
        this.operationEvidence = operationEvidence;
    }

    @Nullable
    public List<DiscountCodeDTO> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(@Nullable List<DiscountCodeDTO> discountCodes) {
        this.discountCodes = discountCodes;
    }

    public Archive getArchive() {
        return archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }
}
