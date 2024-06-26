package pl.strefakursow.eLunchApp1.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.strefakursow.eLunchApp1.DTO.EmployeeDTO;
import pl.strefakursow.eLunchApp1.model.Dish;
import pl.strefakursow.eLunchApp1.model.Employee;
import pl.strefakursow.eLunchApp1.model.EmployeeBuilder;
import pl.strefakursow.eLunchApp1.repo.DishRepo;
import pl.strefakursow.eLunchApp1.repo.EmployeeRepo;
import pl.strefakursow.eLunchApp1.repo.MenuItemRepo;
import pl.strefakursow.eLunchApp1.repo.ProductRepo;
import pl.strefakursow.eLunchApp1.utils.ConverterUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.strefakursow.eLunchApp1.utils.ConverterUtils.convert;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }


    @Override
    public List<EmployeeDTO> getAll() {
        return employeeRepo.findAll().stream()
                .map(ConverterUtils::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void put(UUID uuid, EmployeeDTO employeeDTO) {
        if (!Objects.equal(employeeDTO.getUuid(), uuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeRepo.findByUuid(employeeDTO.getUuid())
                .orElseGet(() -> newEmployee(uuid));

        employee.setPersonalData(convert(employeeDTO.getPersonalData()));
        employee.setLogginData(convert(employeeDTO.getLogginData()));
        employee.setArchive(employeeDTO.getArchive());

        if (employee.getId() == null) {
            employeeRepo.save(employee);
        }
    }

    @Override
    public void delete(UUID uuid) {

        Employee employee = employeeRepo.findByUuid(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        employeeRepo.delete(employee);
    }

    @Override
    public Optional<EmployeeDTO> getByUuid(UUID uuid) {
        return employeeRepo.findByUuid(uuid).map(ConverterUtils::convert);
    }

    private Employee newEmployee(UUID uuid) {
        return new EmployeeBuilder()
                .withUuid(uuid)
                .build();
    }
}
