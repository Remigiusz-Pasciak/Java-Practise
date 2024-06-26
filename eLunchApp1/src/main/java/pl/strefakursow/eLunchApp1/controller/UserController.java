package pl.strefakursow.eLunchApp1.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.DeliveryAddressDTO;
import pl.strefakursow.eLunchApp1.DTO.DiscountCodeDTO;
import pl.strefakursow.eLunchApp1.DTO.LogginDataDTO;
import pl.strefakursow.eLunchApp1.DTO.OperationEvidenceDTO;
import pl.strefakursow.eLunchApp1.DTO.PersonalDataDTO;
import pl.strefakursow.eLunchApp1.DTO.UserDTO;
import pl.strefakursow.eLunchApp1.events.OperationEvidenceCreator;
import pl.strefakursow.eLunchApp1.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    interface UserListListView extends UserDTO.View.Basic, PersonalDataDTO.View.Basic {}
    interface UserView extends UserDTO.View.Extended, PersonalDataDTO.View.Extended, LogginDataDTO.View.Basic,
            DeliveryAddressDTO.View.Basic, OperationEvidenceDTO.View.Extended, DiscountCodeDTO.View.Extended {}

    interface DataUpdateValidation extends Default, UserDTO.DataUpdateValidation {}
    interface NewOperationValidation extends Default, UserDTO.NewOperationValidation {}

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserController(UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @JsonView(UserListListView.class)
    @GetMapping
    public List<UserDTO> get() {
        return userService.getAll();
    }

    @JsonView(UserView.class)
    @GetMapping("/{uuid}")
    public UserDTO get(@PathVariable UUID uuid) {
        return userService.getByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Validated(DataUpdateValidation.class)
    @PutMapping("/{uuid}")
    public void put(@PathVariable UUID uuid, @RequestBody @Valid UserDTO json) {
        userService.put(uuid, json);
    }

    @Transactional
    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        userService.delete(uuid);
    }

    //TODO postOperation
    @Transactional
    @Validated(NewOperationValidation.class)
    @PostMapping("/{uuid}/new-operation")
    public void postOperation(@PathVariable UUID uuid, @RequestBody @Valid UserDTO json) {
        userService.validateNewOperation(uuid, json);

        OperationEvidenceCreator operationEvidenceCreator = new OperationEvidenceCreator(this, json);
        applicationEventPublisher.publishEvent(operationEvidenceCreator);
    }

    //TODO getUserAdressess
    @JsonView(UserView.class)
    @GetMapping("/{uuid}/delivery-addresses")
    public List<DeliveryAddressDTO> getUserAddresses(@PathVariable UUID uuid) {
        UserDTO userDTO = userService.getByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return userDTO.getDeliveryAddress();
    }
}
