package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public void setPersistence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Creating profession in use case");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer identification, Profession profession) throws NoExistException {
        Profession existing = professionPersistence.findByIdentification(identification);
        if (existing != null) {
            return professionPersistence.save(profession);
        }
        throw new NoExistException("The profession with id " + identification + " does not exist in DB, cannot be edited.");
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        Profession existing = professionPersistence.findByIdentification(identification);
        if (existing != null) {
            return professionPersistence.delete(identification);
        }
        throw new NoExistException("The profession with id " + identification + " does not exist in DB, cannot be dropped.");
    }

    @Override
    public List<Profession> findAll() {
        log.info("Fetching all professions from output port: {}", professionPersistence.getClass());
        return professionPersistence.find();
    }

    @Override
    public Profession findOne(Integer identification) throws NoExistException {
        Profession existing = professionPersistence.findByIdentification(identification);
        if (existing != null) {
            return existing;
        }
        throw new NoExistException("The profession with id " + identification + " does not exist in DB.");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public List<Study> getStudies(Integer identification) throws NoExistException {
        Profession existing = professionPersistence.findByIdentification(identification);
        if (existing != null) {
            return existing.getStudies(); // Solo si `getStudies()` está implementado en la entidad
        }
        throw new NoExistException("The profession with id " + identification + " does not exist, cannot get studies.");
    }
}
