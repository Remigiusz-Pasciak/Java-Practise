package pl.strefakursow.eLunchApp1.controller;

import com.google.common.truth.Truth8;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import pl.strefakursow.eLunchApp1.DTO.DelivererDTO;
import pl.strefakursow.eLunchApp1.DTO.DeliveryAddressDTO;
import pl.strefakursow.eLunchApp1.config.JPAConfiguration;
import pl.strefakursow.eLunchApp1.model.DeliveryAddress;
import pl.strefakursow.eLunchApp1.model.User;
import pl.strefakursow.eLunchApp1.model.enums.Archive;
import pl.strefakursow.eLunchApp1.model.enums.Sex;
import pl.strefakursow.eLunchApp1.repo.DelivererRepo;
import pl.strefakursow.eLunchApp1.repo.DeliveryAddressRepo;
import pl.strefakursow.eLunchApp1.repo.OrderRepo;
import pl.strefakursow.eLunchApp1.repo.UserRepo;
import pl.strefakursow.eLunchApp1.service.DelivererService;
import pl.strefakursow.eLunchApp1.service.DelivererServiceImpl;
import pl.strefakursow.eLunchApp1.service.DeliveryAddressService;
import pl.strefakursow.eLunchApp1.service.DeliveryAddressServiceImpl;
import pl.strefakursow.eLunchApp1.utils.AssertionUtils;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;
import pl.strefakursow.eLunchApp1.utils.TestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        JPAConfiguration.class,
        DeliveryAddressControllerTest.Config.class
})
class DeliveryAddressControllerTest {

    @Configuration
    public static class Config {
        @Bean
        public DeliveryAddressService deliveryAddressService(DeliveryAddressRepo deliveryAddressRepo, UserRepo userRepo){
            return new DeliveryAddressServiceImpl(deliveryAddressRepo, userRepo);
        }

        @Bean
        public DeliveryAddressController deliveryAddressController( DeliveryAddressService deliveryAddressService ){
            return new DeliveryAddressController(deliveryAddressService);
        }

    }

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DeliveryAddressRepo deliveryAddressRepo;

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private DeliveryAddressController deliveryAddressController;

    @Autowired
    private PlatformTransactionManager txManager;

    private static final String STR_UUID = "0d4e5d32-b612-45b4-8f35-b018e9bbeccd";


    //add
    @Test
    @Transactional
    public void put1() {

       User user = TestUtils.user("85692ee3-0b44-4d49-b13e-23e2c8d07435",
                TestUtils.personalData("John", "Smith", Sex.MALE, "501-100-100", "john@wp.pl"), null,
                TestUtils.logginData("jsmith", "password"), Archive.CURRENT);

       userRepo.save(user);

        DeliveryAddressDTO deliveryAddressJson = TestUtils.deliveryAddressDTO(STR_UUID, "My address", "Street",
                "51", "", "00-000", "Warsaw", null, "Poland", null, ConverterUtils.convert(user));

        deliveryAddressController.put(UUID.fromString(STR_UUID), deliveryAddressJson);

        DeliveryAddressDTO deliveryAddressDB = deliveryAddressService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
        AssertionUtils.assertEquals(deliveryAddressJson, deliveryAddressDB);
    }

    //update
    @Test
    @Transactional
    public void put2() {

        User user = TestUtils.user("85692ee3-0b44-4d49-b13e-23e2c8d07435",
                TestUtils.personalData("John", "Smith", Sex.MALE, "501-100-100", "john@wp.pl"), null,
                TestUtils.logginData("jsmith", "password"), Archive.CURRENT);
        userRepo.save(user);

        DeliveryAddress deliveryAddress = TestUtils.deliveryAddress(STR_UUID, "My address", "Street",
                "51", "", "00-000", "Warsaw", null, "Poland", null, user);
        deliveryAddressRepo.save(deliveryAddress);

        DeliveryAddressDTO deliveryAddressJson = TestUtils.deliveryAddressDTO(STR_UUID, "My address1", "Street1",
                "511", "1", "00-001", "Warsaw1", "1", "Poland1", "1", ConverterUtils.convert(user));

        deliveryAddressController.put(UUID.fromString(STR_UUID), deliveryAddressJson);

        DeliveryAddressDTO deliveryAddressDB = deliveryAddressService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
        AssertionUtils.assertEquals(deliveryAddressJson, deliveryAddressDB);

    }

    @Test
    @Transactional
    public void delete() {

        TransactionStatus status1 = txManager.getTransaction(TransactionDefinition.withDefaults());
        User user = TestUtils.user("85692ee3-0b44-4d49-b13e-23e2c8d07435",
                TestUtils.personalData("John", "Smith", Sex.MALE, "501-100-100", "john@wp.pl"), null,
                TestUtils.logginData("jsmith", "password"), Archive.CURRENT);
        userRepo.save(user);

        DeliveryAddress deliveryAddress = TestUtils.deliveryAddress(STR_UUID, "My address", "Street",
                "51", "", "00-000", "Warsaw", null, "Poland", null, user);
        txManager.commit(status1);

        TransactionStatus status2 = txManager.getTransaction(TransactionDefinition.withDefaults());
        deliveryAddressController.delete(UUID.fromString(STR_UUID));
        txManager.commit(status2);


        TransactionStatus status3 = txManager.getTransaction(TransactionDefinition.withDefaults());
        Truth8.assertThat(deliveryAddressService.getByUuid(UUID.fromString(STR_UUID))).isEmpty();
        txManager.commit(status3);
    }


}