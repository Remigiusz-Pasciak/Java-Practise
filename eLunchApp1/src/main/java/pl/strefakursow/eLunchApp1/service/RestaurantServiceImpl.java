package pl.strefakursow.eLunchApp1.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.RestaurantDTO;
import pl.strefakursow.eLunchApp1.model.Product;
import pl.strefakursow.eLunchApp1.model.Restaurant;
import pl.strefakursow.eLunchApp1.model.RestaurantBuilder;
import pl.strefakursow.eLunchApp1.repo.DishRepo;
import pl.strefakursow.eLunchApp1.repo.IngredientRepo;
import pl.strefakursow.eLunchApp1.repo.ProductRepo;
import pl.strefakursow.eLunchApp1.repo.RestaurantRepo;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.strefakursow.eLunchApp1.utils.ConverterUtils.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepo restaurantRepo;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepo restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }


    @Override
    public List<RestaurantDTO> getAll() {
        return restaurantRepo.findAll().stream()
                .map(ConverterUtils::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void put(UUID uuid, RestaurantDTO restaurantDTO) {

        if (!Objects.equal(restaurantDTO.getUuid(), uuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Restaurant restaurant = restaurantRepo.findByUuid(restaurantDTO.getUuid())
                .orElseGet(() -> newRestaurant(uuid));

        restaurant.setName(restaurantDTO.getName());
        restaurant.setLogginData(convert(restaurantDTO.getLogginData()));
        restaurant.setCompanyData(convert(restaurantDTO.getCompanyData()));
        restaurant.setOpenTimes(convertOpenTimeDTOS(restaurantDTO.getOpenTimeDTOS()));
        restaurant.setMenu(convertMenuItemDTOS(restaurantDTO.getMenuItemDTOS()));
        restaurant.setDiscountCodes(convertDiscountCodeDTOS(restaurantDTO.getDiscountCodes()));
        restaurant.setArchive(restaurantDTO.getArchive());

        if (restaurant.getId() == null) {
            restaurantRepo.save(restaurant);
        }
    }

    @Override
    public void delete(UUID uuid) {

        Restaurant restaurant = restaurantRepo.findByUuid(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        restaurantRepo.delete(restaurant);

    }

    @Override
    public Optional<RestaurantDTO> getByUuid(UUID uuid) {
        return restaurantRepo.findByUuid(uuid).map(ConverterUtils::convert);
    }

    private Restaurant newRestaurant(UUID uuid) {
        return new RestaurantBuilder()
                .withUuid(uuid)
                .build();
    }
}
