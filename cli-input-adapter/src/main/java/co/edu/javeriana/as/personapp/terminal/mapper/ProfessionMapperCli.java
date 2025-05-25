package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

@Mapper
public class ProfessionMapperCli {

    public ProfessionModelCli fromDomainToAdapterCli(Profession profession) {
        return new ProfessionModelCli(
                profession.getIdentification(),
                profession.getName(),
                profession.getDescription()
        );
    }

    public Profession fromAdapterCliToDomain(ProfessionModelCli model) {
        return new Profession(
                model.getIdentification(),
                model.getName(),
                model.getDescription(),
                null // List<Study> can be null in CLI context
        );
    }
}
