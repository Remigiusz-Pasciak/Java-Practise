package pl.strefakursow.eLunchApp1.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.DelivererDTO;
import pl.strefakursow.eLunchApp1.DTO.OrderDTO;
import pl.strefakursow.eLunchApp1.model.Deliverer;
import pl.strefakursow.eLunchApp1.model.DelivererBuilder;
import pl.strefakursow.eLunchApp1.model.Order;
import pl.strefakursow.eLunchApp1.repo.DelivererRepo;
import pl.strefakursow.eLunchApp1.repo.OrderRepo;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.strefakursow.eLunchApp1.utils.ConverterUtils.convert;

@Service
@CacheConfig(cacheNames = "deliverers")
public class DelivererServiceImpl implements DelivererService {
    private final DelivererRepo delivererRepo;
    private final OrderRepo orderRepo;

    @Autowired
    public DelivererServiceImpl(DelivererRepo delivererRepo, OrderRepo orderRepo) {
        this.delivererRepo = delivererRepo;
        this.orderRepo = orderRepo;
    }


    @Override
    @Cacheable(cacheNames = "deliverers")
    public List<DelivererDTO> getAll() {
        return delivererRepo.findAll().stream()
                .map(ConverterUtils::convert)
                .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "deliverers", allEntries = true)
    @Override
    public void put(UUID uuid, DelivererDTO delivererDTO) {
        if (!Objects.equal(delivererDTO.getUuid(), uuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Deliverer deliverer = delivererRepo.findByUuid(delivererDTO.getUuid())
                .orElseGet(() -> newDeliverer(delivererDTO.getUuid()));

        List<Order> orders = new ArrayList<>();
        if (delivererDTO.getOrders() != null) {
            for (OrderDTO o : delivererDTO.getOrders()) {
                Order order = orderRepo.findByUuid(o.getUuid())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                orders.add(order);
            }
        }

        deliverer.setPersonalData(convert(delivererDTO.getPersonalData()));
        deliverer.setLogginData(convert(delivererDTO.getLogginData()));
        deliverer.setArchive(delivererDTO.getArchive());
        deliverer.setOrders(orders);

        if (deliverer.getId() == null) {
            delivererRepo.save(deliverer);
        }
    }

    @CacheEvict(cacheNames = "deliverers", allEntries = true)
    @Override
    public void delete(UUID uuid) {
        Deliverer deliverer = delivererRepo.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        delivererRepo.delete(deliverer);
    }

    @Override
    public Optional<DelivererDTO> getByUuid(UUID uuid) {
        return delivererRepo.findByUuid(uuid).map(ConverterUtils::convert);
    }


    public static Deliverer newDeliverer(UUID uuid) {
        return new DelivererBuilder()
                .withUuid(uuid)
                .build();
    }
}
