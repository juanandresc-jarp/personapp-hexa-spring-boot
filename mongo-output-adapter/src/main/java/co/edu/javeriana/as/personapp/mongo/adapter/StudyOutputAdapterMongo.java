package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.StudyRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private StudyRepositoryMongo studyRepository;

    @Autowired
    private EstudiosMapperMongo mapper;

    @Override
    public Study save(Study study) {
        EstudiosDocument savedDoc = studyRepository.save(mapper.fromDomainToAdapter(study));
        return mapper.fromAdapterToDomain(savedDoc);
    }

    @Override
    public boolean delete(Integer cc, Integer professionId) {
        String id = cc + "-" + professionId;
        var doc = studyRepository.findById(id);
        if (doc.isPresent()) {
            studyRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public List<Study> find() {
        return studyRepository.findAll().stream()
                .map(mapper::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer cc, Integer professionId) {
        String id = cc + "-" + professionId;
        return mapper.fromAdapterToDomain(
            studyRepository.findById(id).orElse(null)
        );
    }

}
