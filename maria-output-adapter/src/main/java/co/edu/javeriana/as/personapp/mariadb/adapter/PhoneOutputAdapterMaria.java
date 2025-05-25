package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("phoneOutputAdapterMaria")
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Saving phone in MariaDB: {}", phone);
        TelefonoEntity entity = telefonoMapperMaria.fromDomainToAdapter(phone);
        TelefonoEntity saved = telefonoRepositoryMaria.save(entity);
        return telefonoMapperMaria.fromAdapterToDomain(saved);
    }

    @Override
    public boolean delete(String number) {
        if (telefonoRepositoryMaria.existsById(number)) {
            telefonoRepositoryMaria.deleteById(number);
            return true;
        }
        return false;
    }

    @Override
    public List<Phone> find() {
        return telefonoRepositoryMaria.findAll().stream()
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        return telefonoRepositoryMaria.findById(number)
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
