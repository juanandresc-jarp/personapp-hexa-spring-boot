package co.edu.javeriana.as.personapp.adapter;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.Response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private PersonaMapperRest personaMapperRest;

    private PersonInputPort personInputPort;
    private String currentDatabase;

    private void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            currentDatabase = DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            currentDatabase = DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<PersonaResponse> historial(String database) {
        try {
            setPersonOutputPortInjection(database);
            return personInputPort.findAll().stream()
                    .map(person -> personaMapperRest.fromDomainToAdapterRest(person, currentDatabase))
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            log.error("Database inválida: {}", e.getMessage());
            return List.of();
        }
    }

    public ResponseEntity<?> crearPersona(PersonaRequest request, String database) {
        try {
            setPersonOutputPortInjection(database);
            Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
            return ResponseEntity.ok(personaMapperRest.fromDomainToAdapterRest(person, currentDatabase));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("500", "Error creando la persona", LocalDateTime.now()));
        }
    }

    public ResponseEntity<?> obtenerPersona(String database, int cc) {
        try {
            setPersonOutputPortInjection(database);
            Person person = personInputPort.findOne(cc);
            return ResponseEntity.ok(personaMapperRest.fromDomainToAdapterRest(person, currentDatabase));
        } catch (NoExistException e) {
            return ResponseEntity.status(404).body(new Response("404", e.getMessage(), LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("500", "Error interno", LocalDateTime.now()));
        }
    }

    public ResponseEntity<?> actualizarPersona(String database, int cc, PersonaRequest request) {
        try {
            setPersonOutputPortInjection(database);
            Person updated = personInputPort.edit(cc, personaMapperRest.fromAdapterToDomain(request));
            return ResponseEntity.ok(personaMapperRest.fromDomainToAdapterRest(updated, currentDatabase));
        } catch (NoExistException e) {
            return ResponseEntity.status(404).body(new Response("404", e.getMessage(), LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("500", "Error actualizando persona", LocalDateTime.now()));
        }
    }

    public ResponseEntity<?> eliminarPersona(String database, int cc) {
        try {
            setPersonOutputPortInjection(database);
            personInputPort.drop(cc);
            return ResponseEntity.ok(new Response("200", "Persona eliminada", LocalDateTime.now()));
        } catch (NoExistException e) {
            return ResponseEntity.status(404).body(new Response("404", e.getMessage(), LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("500", "Error eliminando persona", LocalDateTime.now()));
        }
    }
}
