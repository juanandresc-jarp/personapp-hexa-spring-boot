package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;
    private PersonOutputPort personPersistence;
    private ProfessionOutputPort professionPersistence;

    // Constructor para inyectar dependencias
    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence,
                        @Qualifier("personOutputAdapterMaria") PersonOutputPort personPersistence,
                        @Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
        this.studyPersistence = studyPersistence;
        this.personPersistence = personPersistence;
        this.professionPersistence = professionPersistence;
    }

    // Establecer la persistencia (usado para inyección dinámica si se necesita)
    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    // Crear un nuevo estudio
    @Override
    public Study create(Study study) {
        log.debug("Creating study in use case");


        Person person = personPersistence.findById(study.getPerson().getIdentification());
        Profession profession = professionPersistence.findByIdentification(study.getProfession().getIdentification());

        study.setPerson(person);
        study.setProfession(profession);

        // Guardar y retornar el estudio
        return studyPersistence.save(study);
    }

    // Editar un estudio existente
    @Override
    public Study edit(Study study) throws NoExistException {
        Integer cc = study.getPerson().getIdentification();
        Integer professionId = study.getProfession().getIdentification();
        Study oldStudy = studyPersistence.findById(cc, professionId);

        if (oldStudy != null) {
            return studyPersistence.save(study);
        }

        throw new NoExistException("The study with person ID " + cc + " and profession ID " + professionId + " does not exist");
    }

    // Eliminar un estudio
    @Override
    public Boolean drop(Integer cc, Integer professionId) throws NoExistException {
        Study oldStudy = studyPersistence.findById(cc, professionId);

        if (oldStudy != null) {
            return studyPersistence.delete(cc, professionId);
        }

        throw new NoExistException("The study with person ID " + cc + " and profession ID " + professionId + " does not exist");
    }

    // Obtener todos los estudios
    @Override
    public List<Study> findAll() {
        return studyPersistence.find();
    }

    // Buscar un estudio por ID de persona y profesión
    @Override
    public Study findOne(Integer cc, Integer professionId) throws NoExistException {
        Study study = studyPersistence.findById(cc, professionId);
        if (study != null) {
            return study;
        }
        throw new NoExistException("The study with person ID " + cc + " and profession ID " + professionId + " does not exist");
    }

    // Contar el número total de estudios
    @Override
    public Integer count() {
        return studyPersistence.find().size();
    }
}
