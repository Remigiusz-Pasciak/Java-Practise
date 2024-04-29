package pl.strefakursow.eLunchApp1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.strefakursow.eLunchApp1.model.OperationEvidence;
import pl.strefakursow.eLunchApp1.model.User;
import pl.strefakursow.eLunchApp1.repo.OpenTimeRepo;
import pl.strefakursow.eLunchApp1.repo.OperationEvidenceRepo;
import pl.strefakursow.eLunchApp1.repo.RestaurantRepo;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OperationEvidenceServiceImpl implements OperationEvidenceService {
    private final OperationEvidenceRepo operationEvidenceRepo;

    @Autowired
    public OperationEvidenceServiceImpl(OperationEvidenceRepo operationEvidenceRepo) {
        this.operationEvidenceRepo = operationEvidenceRepo;
    }


    @Override
    public List<OperationEvidence> getAll() {
        return operationEvidenceRepo.findAll();
    }

    @Override
    public void add( OperationEvidence operationEvidence) {
        operationEvidenceRepo.save(operationEvidence);

    }

    @Override
    public void delete(OperationEvidence operationEvidence) {
        operationEvidenceRepo.delete(operationEvidence);

    }


    @Override
    public BigDecimal getUserAccountBalance(User user) {
        return operationEvidenceRepo.getUserAccountBalance(user);
    }

    @Override
    public BigDecimal getAccountBalanceAfterOperation(OperationEvidence operationEvidence) {
        BigDecimal balanceBefore = getUserAccountBalance(operationEvidence.getUser());
        BigDecimal balanceAfter;

        switch (operationEvidence.getType()){
            case WITHDRAW:
            case PAYMENT:
                balanceAfter = balanceBefore.subtract(operationEvidence.getAmount());
                break;
            case DEPOSIT:
                balanceAfter = balanceBefore.add(operationEvidence.getAmount());
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return balanceAfter;
    }
}
