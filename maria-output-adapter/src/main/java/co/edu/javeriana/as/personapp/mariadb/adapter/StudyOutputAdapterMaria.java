package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudioRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("studyOutputAdapterMaria")
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private EstudioRepositoryMaria estudioRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Override
    public Study save(Study study) {
        log.debug("Saving study in MariaDB: {}", study);
        System.out.println("Saving study in MariaDB: " + study);
        EstudiosEntity entity = estudiosMapperMaria.fromDomainToAdapter(study);
        EstudiosEntity saved = estudioRepositoryMaria.save(entity);
        return estudiosMapperMaria.fromAdapterToDomain(saved);
    }

    @Override
    public boolean delete(Integer cc, Integer professionId) {
        EstudiosEntityPK pk = new EstudiosEntityPK();
        pk.setCcPer(cc);
        pk.setIdProf(professionId);
        if (estudioRepositoryMaria.existsById(pk)) {
            estudioRepositoryMaria.deleteById(pk);
            return true;
        }
        return false;
    }

    @Override
    public List<Study> find() {
        return estudioRepositoryMaria.findAll().stream()
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer cc, Integer professionId) {
        EstudiosEntityPK pk = new EstudiosEntityPK();
        pk.setCcPer(cc);
        pk.setIdProf(professionId);
        return estudioRepositoryMaria.findById(pk)
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
