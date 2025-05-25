package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMongo profesionRepositoryMongo;

    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;

    @Override
    public Profession save(Profession profession) {
        log.debug("Saving profession in MongoDB");
        try {
            ProfesionDocument document = profesionMapperMongo.fromDomainToAdapter(profession);
            ProfesionDocument saved = profesionRepositoryMongo.save(document);
            return profesionMapperMongo.fromAdapterToDomain(saved);
        } catch (MongoWriteException e) {
            log.warn("Error writing to MongoDB: {}", e.getMessage());
            return profession;
        }
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Deleting profession in MongoDB: {}", identification);
        profesionRepositoryMongo.deleteById(identification);
        return profesionRepositoryMongo.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("Fetching all professions from MongoDB");
        return profesionRepositoryMongo.findAll().stream()
                .map(profesionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findByIdentification(Integer identification) {
        log.debug("Finding profession by ID in MongoDB: {}", identification);
        return profesionRepositoryMongo.findById(identification)
                .map(profesionMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
