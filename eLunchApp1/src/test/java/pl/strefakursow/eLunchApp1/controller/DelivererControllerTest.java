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
import pl.strefakursow.eLunchApp1.config.JPAConfiguration;
import pl.strefakursow.eLunchApp1.model.Deliverer;
import pl.strefakursow.eLunchApp1.model.enums.Archive;
import pl.strefakursow.eLunchApp1.model.enums.Sex;
import pl.strefakursow.eLunchApp1.repo.DelivererRepo;
import pl.strefakursow.eLunchApp1.repo.OrderRepo;
import pl.strefakursow.eLunchApp1.service.DelivererService;
import pl.strefakursow.eLunchApp1.service.DelivererServiceImpl;
import pl.strefakursow.eLunchApp1.utils.AssertionUtils;
import pl.strefakursow.eLunchApp1.utils.TestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        JPAConfiguration.class,
        DelivererControllerTest.Config.class
})
class DelivererControllerTest {
    @Configuration
    public static class Config {
        @Bean
        public DelivererService delivererService(DelivererRepo delivererRepo, OrderRepo orderRepo){
            return new DelivererServiceImpl(delivererRepo, orderRepo);
        }

        @Bean
        public DelivererController delivererController( DelivererService delivererService ){
            return new DelivererController(delivererService);
        }

    }

    @Autowired
    private DelivererRepo delivererRepo;

    @Autowired
    private DelivererService delivererService;

    @Autowired
    private DelivererController delivererController;

    @Autowired
    private PlatformTransactionManager txManager;

    private static final String STR_UUID = "742ab078-219a-4691-bcc0-8415589cea65";


    // add
    @Test
    @Transactional
    public void put1() {

        DelivererDTO delivererJson = TestUtils.delivererDTO(STR_UUID,
                TestUtils.personalDataDTO("John", "Smith", Sex.MALE, "501-100-100", "john@wp.pl"),
                TestUtils.logginDataDTO("jsmith", "password"), Archive.CURRENT);

        delivererController.put(UUID.fromString(STR_UUID), delivererJson);

        DelivererDTO delivererDB = delivererService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
        AssertionUtils.assertEquals(delivererJson, delivererDB);
    }

    //update
    @Test
    @Transactional
    public void put2() {

        Deliverer deliverer = TestUtils.deliverer(STR_UUID,
                TestUtils.personalData("John", "Smith", Sex.MALE, "501-100-100", "john@wp.pl"),
                TestUtils.logginData("jsmith", "password"), Archive.CURRENT);
        delivererRepo.save(deliverer);


        DelivererDTO delivererJson = TestUtils.delivererDTO(STR_UUID,
                TestUtils.personalDataDTO("John1", "Smith1", Sex.FEMALE, "501-200-200", "john512@wp.pl"),
                TestUtils.logginDataDTO("jsmith1", "password1"), Archive.ARCHIVE);
        delivererController.put(UUID.fromString(STR_UUID), delivererJson);

        DelivererDTO delivererDB = delivererService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
        AssertionUtils.assertEquals(delivererJson, delivererDB);

    }

    @Test
    @Transactional
    public void delete() {

        TransactionStatus status1 = txManager.getTransaction(TransactionDefinition.withDefaults());
        Deliverer deliverer = TestUtils.deliverer(STR_UUID,
                TestUtils.personalData("John", "Smith", Sex.MALE, "501-100-100", "john@wp.pl"),
                TestUtils.logginData("jsmith", "password"), Archive.CURRENT);
        delivererRepo.save(deliverer);
        txManager.commit(status1);

        TransactionStatus status2 = txManager.getTransaction(TransactionDefinition.withDefaults());
        delivererController.delete(UUID.fromString(STR_UUID));
        txManager.commit(status2);


        TransactionStatus status3 = txManager.getTransaction(TransactionDefinition.withDefaults());
        Truth8.assertThat(delivererService.getByUuid(UUID.fromString(STR_UUID))).isEmpty();
        txManager.commit(status3);
    }


}