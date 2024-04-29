package pl.strefakursow.eLunchApp1.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.DelivererDTO;
import pl.strefakursow.eLunchApp1.DTO.DiscountCodeDTO;
import pl.strefakursow.eLunchApp1.DTO.DishDTO;
import pl.strefakursow.eLunchApp1.DTO.MenuItemDTO;
import pl.strefakursow.eLunchApp1.DTO.PeriodDTO;
import pl.strefakursow.eLunchApp1.DTO.ProductDTO;
import pl.strefakursow.eLunchApp1.service.DelivererService;
import pl.strefakursow.eLunchApp1.service.DishService;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/api/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    interface DishListListView extends DishDTO.View.Basic {}
    interface DishView extends DishDTO.View.Extended, ProductDTO.View.Extended, MenuItemDTO.View.Basic {}

    interface DishDataUpdateValidation extends Default, DishDTO.DataUpdateValidation {}

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @JsonView(DishListListView.class)
    @GetMapping
    public List<DishDTO> get() {
        return dishService.getAll();
    }

    @JsonView(DishView.class)
    @GetMapping("/{uuid}")
    public DishDTO get(@PathVariable UUID uuid) {
        return dishService.getByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Validated(DishDataUpdateValidation.class)
    @PutMapping("/{uuid}")
    public void put(@PathVariable UUID uuid, @RequestBody @Valid DishDTO json) {
        dishService.put(uuid, json);
    }

    @Transactional
    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        dishService.delete(uuid);
    }
}
