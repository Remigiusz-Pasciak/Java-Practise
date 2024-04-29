package pl.strefakursow.eLunchApp1.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.OpenTimeDTO;
import pl.strefakursow.eLunchApp1.model.MenuItem;
import pl.strefakursow.eLunchApp1.model.OpenTime;
import pl.strefakursow.eLunchApp1.model.OpenTimeBuilder;
import pl.strefakursow.eLunchApp1.model.Restaurant;
import pl.strefakursow.eLunchApp1.repo.DishRepo;
import pl.strefakursow.eLunchApp1.repo.MenuItemRepo;
import pl.strefakursow.eLunchApp1.repo.OpenTimeRepo;
import pl.strefakursow.eLunchApp1.repo.RestaurantRepo;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.strefakursow.eLunchApp1.utils.ConverterUtils.convert;

@Service
public class OpenTimeServiceImpl implements OpenTimeService {
    private final OpenTimeRepo openTimeRepo;
    private final RestaurantRepo restaurantRepo;

    @Autowired
    public OpenTimeServiceImpl(OpenTimeRepo openTimeRepo, RestaurantRepo restaurantRepo) {
        this.openTimeRepo = openTimeRepo;
        this.restaurantRepo = restaurantRepo;
    }


    @Override
    public List<OpenTimeDTO> getAll() {
        return openTimeRepo.findAll().stream()
                .map(ConverterUtils::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void put(UUID uuid, OpenTimeDTO openTimeDTO) {
        if (!Objects.equal(openTimeDTO.getUuid(), uuid)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

      Restaurant restaurant = restaurantRepo.findByUuid(openTimeDTO.getRestaurantDTO().getUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

      OpenTime openTime = openTimeRepo.findByUuid(openTimeDTO.getUuid())
                .orElseGet(() -> newOpenTime(uuid, restaurant));

        if (!Objects.equal(openTime.getRestaurant().getUuid(), openTimeDTO.getRestaurantDTO().getUuid())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        openTime.setDayOfWeek(openTimeDTO.getDayOfWeek());
        openTime.setPeriodTime(convert(openTimeDTO.getPeriodTimeDTO()));

        if (openTime.getId() == null){
            openTimeRepo.save(openTime);
        }

    }


    @Override
    public void delete(UUID uuid) {

        OpenTime openTime = openTimeRepo.findByUuid(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        openTimeRepo.delete(openTime);

    }

    @Override
    public Optional<OpenTimeDTO> getByUuid(UUID uuid) {
        return openTimeRepo.findByUuid(uuid).map(ConverterUtils::convert);
    }

    private OpenTime newOpenTime(UUID uuid, Restaurant restaurant) {
        return new OpenTimeBuilder()
                .withUuid(uuid)
                .withRestaurant(restaurant)
                .build();
    }

}
