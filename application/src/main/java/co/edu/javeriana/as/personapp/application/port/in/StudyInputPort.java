package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {

    void setPersistence(StudyOutputPort studyPersistence);

    Study create(Study study);

    Study edit(Study study) throws NoExistException;

    Boolean drop(Integer cc, Integer professionId) throws NoExistException;

    List<Study> findAll();

    Study findOne(Integer cc, Integer professionId) throws NoExistException;

    Integer count();
}
