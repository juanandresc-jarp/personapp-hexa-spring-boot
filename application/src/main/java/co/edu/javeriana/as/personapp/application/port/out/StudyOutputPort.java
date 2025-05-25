package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyOutputPort {

    Study save(Study study);

    boolean delete(Integer cc, Integer professionId);

    List<Study> find();

    Study findById(Integer cc, Integer professionId);
}
