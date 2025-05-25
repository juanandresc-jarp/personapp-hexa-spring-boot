package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PhoneRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    @Autowired
    private PhoneRepositoryMongo phoneRepository;

    @Autowired
    private TelefonoMapperMongo mapper;

    @Override
    public Phone save(Phone phone) {
        return mapper.fromAdapterToDomain(
            phoneRepository.save(mapper.fromDomainToAdapter(phone))
        );
    }

    @Override
    public boolean delete(String number) {
        if (phoneRepository.existsById(number)) {
            phoneRepository.deleteById(number);
            return true;
        }
        return false;
    }

    @Override
    public List<Phone> find() {
        return phoneRepository.findAll().stream()
                .map(mapper::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        return phoneRepository.findById(number)
                .map(mapper::fromAdapterToDomain)
                .orElse(null);
    }
}
