package co.edu.javeriana.as.personapp.terminal.mapper;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Component
public class PersonaMapperCli {

    public PersonaModelCli fromDomainToAdapterCli(Person person) {
        return new PersonaModelCli(
                person.getIdentification(),
                person.getFirstName(),
                person.getLastName(),
                person.getGender().toString(),
                person.getAge()
        );
    }

    public Person fromAdapterCliToDomain(PersonaModelCli cliModel) {
        Gender gender = Gender.valueOf(cliModel.getGenero().toUpperCase());
        return new Person(
                cliModel.getCc(),
                cliModel.getNombre(),
                cliModel.getApellido(),
                gender,
                cliModel.getEdad(),
                null,
                null
        );
    }
}
