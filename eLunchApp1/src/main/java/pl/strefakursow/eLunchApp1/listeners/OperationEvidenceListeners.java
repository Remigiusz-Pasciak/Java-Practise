package pl.strefakursow.eLunchApp1.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.UserDTO;
import pl.strefakursow.eLunchApp1.events.OperationEvidenceCreator;
import pl.strefakursow.eLunchApp1.model.OperationEvidence;
import pl.strefakursow.eLunchApp1.model.User;
import pl.strefakursow.eLunchApp1.repo.UserRepo;
import pl.strefakursow.eLunchApp1.service.OperationEvidenceService;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;

import java.math.BigDecimal;

@Component
public class OperationEvidenceListeners {

    private final OperationEvidenceService operationEvidenceService;
    private final UserRepo userRepo;

    @Autowired
    public OperationEvidenceListeners(OperationEvidenceService operationEvidenceService, UserRepo userRepo) {
        this.operationEvidenceService = operationEvidenceService;
        this.userRepo = userRepo;
    }

    @EventListener
    public void onAddOperation(OperationEvidenceCreator operationEvidenceCreator){
        UserDTO userDTO = operationEvidenceCreator.getUserDTO();

       OperationEvidence operationEvidence = ConverterUtils.convert(userDTO.getOperationEvidence().stream().findFirst().orElseThrow());
       User user = userRepo.findByUuid(userDTO.getUuid()).orElseThrow();
       operationEvidence.setUser(user);

       validateAccountBalanceAfterOperation(operationEvidence);
       operationEvidenceService.add(operationEvidence);

    }

    private void validateAccountBalanceAfterOperation(OperationEvidence operationEvidence){
        BigDecimal accountBalanceAfterOperation = operationEvidenceService.getAccountBalanceAfterOperation(operationEvidence);
        if (accountBalanceAfterOperation.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
