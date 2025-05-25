package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfessionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfessionRepositoryMaria professionRepositoryMaria;

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Override
    public Profession save(Profession profession) {
        log.debug("Saving profession to MariaDB");
        ProfesionEntity entity = profesionMapperMaria.fromDomainToAdapter(profession);
        ProfesionEntity saved = professionRepositoryMaria.save(entity);
        return profesionMapperMaria.fromAdapterToDomain(saved);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Deleting profession from MariaDB: {}", identification);
        professionRepositoryMaria.deleteById(identification);
        return professionRepositoryMaria.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("Fetching all professions from MariaDB");
        return professionRepositoryMaria.findAll().stream()
                .map(profesionMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findByIdentification(Integer identification) {
        log.debug("Finding profession by ID in MariaDB: {}", identification);
        return professionRepositoryMaria.findById(identification)
                .map(profesionMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
