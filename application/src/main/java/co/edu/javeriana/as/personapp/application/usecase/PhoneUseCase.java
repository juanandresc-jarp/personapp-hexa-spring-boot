package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersistence;

    public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public void setPersistence(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Creating phone in use case");
        return phonePersistence.save(phone);
    }

    @Override
    public Phone edit(String number, Phone phone) throws NoExistException {
        Phone oldPhone = phonePersistence.findById(number);
        if (oldPhone != null) {
            return phonePersistence.save(phone);
        }
        throw new NoExistException("The phone with number " + number + " does not exist");
    }

    @Override
    public Boolean drop(String number) throws NoExistException {
        Phone oldPhone = phonePersistence.findById(number);
        if (oldPhone != null) {
            return phonePersistence.delete(number);
        }
        throw new NoExistException("The phone with number " + number + " does not exist");
    }

    @Override
    public List<Phone> findAll() {
        return phonePersistence.find();
    }

    @Override
    public Phone findOne(String number) throws NoExistException {
        Phone phone = phonePersistence.findById(number);
        if (phone != null) {
            return phone;
        }
        throw new NoExistException("The phone with number " + number + " does not exist");
    }

    @Override
    public Integer count() {
        return phonePersistence.find().size();
    }
}
