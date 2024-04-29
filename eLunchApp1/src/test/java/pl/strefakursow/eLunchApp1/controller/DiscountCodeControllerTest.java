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
import pl.strefakursow.eLunchApp1.DTO.DeliveryAddressDTO;
import pl.strefakursow.eLunchApp1.DTO.DiscountCodeDTO;
import pl.strefakursow.eLunchApp1.config.JPAConfiguration;
import pl.strefakursow.eLunchApp1.model.DeliveryAddress;
import pl.strefakursow.eLunchApp1.model.DiscountCode;
import pl.strefakursow.eLunchApp1.model.User;
import pl.strefakursow.eLunchApp1.model.enums.Archive;
import pl.strefakursow.eLunchApp1.model.enums.DiscountUnit;
import pl.strefakursow.eLunchApp1.model.enums.Sex;
import pl.strefakursow.eLunchApp1.repo.*;
import pl.strefakursow.eLunchApp1.service.DelivererService;
import pl.strefakursow.eLunchApp1.service.DelivererServiceImpl;
import pl.strefakursow.eLunchApp1.service.DiscountCodeService;
import pl.strefakursow.eLunchApp1.service.DiscountCodeServiceImpl;
import pl.strefakursow.eLunchApp1.utils.AssertionUtils;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;
import pl.strefakursow.eLunchApp1.utils.TestUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        JPAConfiguration.class,
        DiscountCodeControllerTest.Config.class
})
class DiscountCodeControllerTest {

    @Configuration
    public static class Config {
        @Bean
        public DiscountCodeService discountCodeService(DiscountCodeRepo discountCodeRepo, UserRepo userRepo, RestaurantRepo restaurantRepo){
            return new DiscountCodeServiceImpl(discountCodeRepo, userRepo,restaurantRepo );
        }

        @Bean
        public DiscountCodeController discountCodeController( DiscountCodeService discountCodeService ){
            return new DiscountCodeController(discountCodeService);
        }
    }

    @Autowired
    private DiscountCodeRepo discountCodeRepo;

    @Autowired
    private DiscountCodeService discountCodeService;

    @Autowired
    private DiscountCodeController discountCodeController;

    @Autowired
    private PlatformTransactionManager txManager;

    private static final String STR_UUID = "4d5ead8e-fc2c-4dd1-9ad1-c702f6ca67d4";

    @Test
    @Transactional
    public void put1() {


       DiscountCodeDTO discountCodeJson = TestUtils.discountCodeDTO(STR_UUID, "Student", new BigDecimal("25"), DiscountUnit.PERCENT,
               "2020-01-01T00:00:00", "2020-02-01T00:00:00", null, null);

        discountCodeController.put(UUID.fromString(STR_UUID), discountCodeJson);

        DiscountCodeDTO discountCodeDB = discountCodeService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
        AssertionUtils.assertEquals(discountCodeJson, discountCodeDB);
    }

    //update
    @Test
    @Transactional
    public void put2() {

        DiscountCode discountCode = TestUtils.discountCode(STR_UUID, "Student", new BigDecimal("25"), DiscountUnit.PERCENT,
                "2020-01-01T00:00:00", "2020-02-01T00:00:00", null, null);
        discountCodeRepo.save(discountCode);

        DiscountCodeDTO discountCodeJson = TestUtils.discountCodeDTO(STR_UUID, "Student1", new BigDecimal("20"), DiscountUnit.PLN,
                "2020-05-01T00:00:00", "2020-06-01T00:00:00", null, null);

        discountCodeController.put(UUID.fromString(STR_UUID), discountCodeJson);


        DiscountCodeDTO discountCodeDB = discountCodeService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
        AssertionUtils.assertEquals(discountCodeJson, discountCodeDB);


    }

    @Test
    @Transactional
    public void delete() {

        TransactionStatus status1 = txManager.getTransaction(TransactionDefinition.withDefaults());
        DiscountCode discountCode = TestUtils.discountCode(STR_UUID, "Student", new BigDecimal("25"), DiscountUnit.PERCENT,
                "2020-01-01T00:00:00", "2020-02-01T00:00:00", null, null);
        discountCodeRepo.save(discountCode);
        txManager.commit(status1);

        TransactionStatus status2 = txManager.getTransaction(TransactionDefinition.withDefaults());
        discountCodeController.delete(UUID.fromString(STR_UUID));
        txManager.commit(status2);


        TransactionStatus status3 = txManager.getTransaction(TransactionDefinition.withDefaults());
        Truth8.assertThat(discountCodeService.getByUuid(UUID.fromString(STR_UUID))).isEmpty();
        txManager.commit(status3);
    }

}