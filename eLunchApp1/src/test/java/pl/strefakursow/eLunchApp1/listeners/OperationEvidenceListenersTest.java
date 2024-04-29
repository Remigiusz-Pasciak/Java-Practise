package pl.strefakursow.eLunchApp1.listeners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.UserDTO;
import pl.strefakursow.eLunchApp1.config.JPAConfiguration;
import pl.strefakursow.eLunchApp1.controller.DelivererController;
import pl.strefakursow.eLunchApp1.controller.UserController;
import pl.strefakursow.eLunchApp1.model.OperationEvidence;
import pl.strefakursow.eLunchApp1.model.User;
import pl.strefakursow.eLunchApp1.model.enums.Archive;
import pl.strefakursow.eLunchApp1.model.enums.EvidenceType;
import pl.strefakursow.eLunchApp1.model.enums.Sex;
import pl.strefakursow.eLunchApp1.repo.DelivererRepo;
import pl.strefakursow.eLunchApp1.repo.OperationEvidenceRepo;
import pl.strefakursow.eLunchApp1.repo.OrderRepo;
import pl.strefakursow.eLunchApp1.repo.UserRepo;
import pl.strefakursow.eLunchApp1.service.*;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;
import pl.strefakursow.eLunchApp1.utils.TestUtils;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        JPAConfiguration.class,
        OperationEvidenceListenersTest.Config.class
})
class OperationEvidenceListenersTest {

    @Configuration
    public static class Config {
        @Bean
        public OperationEvidenceService operationEvidenceService(OperationEvidenceRepo operationEvidenceRepo){
            return new OperationEvidenceServiceImpl(operationEvidenceRepo);
        }

        @Bean
        public OperationEvidenceListeners operationEvidenceListeners(OperationEvidenceService operationEvidenceService, UserRepo userRepo){
            return new OperationEvidenceListeners(operationEvidenceService, userRepo);
        }

        @Bean
        public UserService userService(UserRepo userRepo){
            return new UserServiceImpl(userRepo);
        }

        @Bean
        public UserController userController(UserService userService, ApplicationEventPublisher applicationEventPublisher){
            return new UserController(userService, applicationEventPublisher);
        }
    }

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OperationEvidenceRepo operationEvidenceRepo;

    @Autowired
    private UserController userController;


    private static final String STR_UUID = "8584a7aa-97de-4836-a2d1-ada856ae4ceb";

    @Test
    @Transactional
    public void deposit() {
        User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
                        Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
                TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
        userRepo.save(user);

        UserDTO userJson = ConverterUtils.convert(user);
        userJson.setOperationEvidence(List.of(
                TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.DEPOSIT, new BigDecimal("100.00"), userJson)
        ));
        userController.postOperation(UUID.fromString(STR_UUID), userJson);

        BigDecimal userAccountBalance = operationEvidenceRepo.getUserAccountBalance(user);
        Assertions.assertEquals(new BigDecimal("100.00"), userAccountBalance);
    }

    @Test
    @Transactional
    public void withdraw() {
        User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
                        Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
                TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
        userRepo.save(user);
        OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T12:00:00Z", EvidenceType.DEPOSIT, new BigDecimal("100.00"), user);
        operationEvidenceRepo.save(operationEvidence);

        UserDTO userJson = ConverterUtils.convert(user);
        userJson.setOperationEvidence(List.of(
                TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.WITHDRAW, new BigDecimal("25.00"), userJson)
        ));
        userController.postOperation(UUID.fromString(STR_UUID), userJson);

        BigDecimal userAccountBalance = operationEvidenceRepo.getUserAccountBalance(user);
        Assertions.assertEquals(new BigDecimal("75.00"), userAccountBalance);
    }

    @Test
    @Transactional
    public void payment() {
        User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
                        Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
                TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
        userRepo.save(user);
        OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T12:00:00Z", EvidenceType.DEPOSIT, new BigDecimal("100.00"), user);
        operationEvidenceRepo.save(operationEvidence);

        UserDTO userJson = ConverterUtils.convert(user);
        userJson.setOperationEvidence(List.of(
                TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.PAYMENT, new BigDecimal("25.00"), userJson)
        ));
        userController.postOperation(UUID.fromString(STR_UUID), userJson);

        BigDecimal userAccountBalance = operationEvidenceRepo.getUserAccountBalance(user);
        Assertions.assertEquals(new BigDecimal("75.00"), userAccountBalance);
    }

    @Test
    @Transactional
    public void minusBalance() {
        User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
                        Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
                TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
        userRepo.save(user);

        UserDTO userJson = ConverterUtils.convert(user);
        userJson.setOperationEvidence(List.of(
                TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.WITHDRAW, new BigDecimal("100.00"), userJson)
        ));
        Assertions.assertThrows(ResponseStatusException.class, () -> userController.postOperation(UUID.fromString(STR_UUID), userJson));
    }
}