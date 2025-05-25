package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.StudyMapperCli;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterCli {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private StudyMapperCli studyMapperCli;

    private StudyInputPort studyInputPort;
    private PersonInputPort personInputPort;
    private ProfessionInputPort professionInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria, personOutputPortMaria, professionOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo, personOutputPortMongo, professionOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial Study in Input Adapter");
        studyInputPort.findAll().stream()
            .map(studyMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }

    public void crear() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID Persona: ");
        int cc = sc.nextInt();
        System.out.print("ID Profesion: ");
        int idProf = sc.nextInt();
        sc.nextLine();
        System.out.print("Fecha de graduacion (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("Universidad: ");
        String universidad = sc.nextLine();

        try {
            // Verificar si la persona y la profesión existen
            Person persona = personInputPort.findOne(cc);
            Profession profesion = professionInputPort.findOne(idProf);
            
            // Si no se encuentra la persona o la profesión, lanzamos una excepción
            if (persona == null) {
                throw new NoExistException("Persona no encontrada.");
            }
            if (profesion == null) {
                throw new NoExistException("Profesion no encontrada.");
            }
            

            // Crear un nuevo estudio
            Study nueva = new Study(
                persona,
                profesion,
                fecha.isEmpty() ? null : LocalDate.parse(fecha),
                universidad
            );

            // Crear el estudio en el UseCase
            Study creada = studyInputPort.create(nueva);
            System.out.println("Estudio creado: " + studyMapperCli.fromDomainToAdapterCli(creada));

        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID Persona: ");
        int cc = sc.nextInt();
        System.out.print("ID Profesion: ");
        int prof = sc.nextInt();
        try {
            studyInputPort.drop(cc, prof);
            System.out.println("Estudio eliminado.");
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID Persona: ");
        int cc = sc.nextInt();
        System.out.print("ID Profesion: ");
        int prof = sc.nextInt();
        try {
            Study encontrada = studyInputPort.findOne(cc, prof);
            System.out.println(studyMapperCli.fromDomainToAdapterCli(encontrada));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void editar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID Persona (del estudio a editar): ");
        int cc = sc.nextInt();
        System.out.print("ID Profesion (del estudio a editar): ");
        int idProf = sc.nextInt();
        sc.nextLine();
        System.out.print("Nueva fecha de graduación (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("Nuevo nombre de universidad: ");
        String universidad = sc.nextLine();

        try {

            Study existente = studyInputPort.findOne(cc, idProf);

            Person persona = personInputPort.findOne(cc);
            Profession profesion = professionInputPort.findOne(idProf);

            if (persona == null || profesion == null) {
                throw new NoExistException("Persona o profesión no encontrada.");
            }

            Study actualizado = new Study(
                persona,
                profesion,
                fecha.isEmpty() ? null : LocalDate.parse(fecha),
                universidad
            );

            Study resultado = studyInputPort.edit(actualizado);
            System.out.println("Estudio actualizado: " + studyMapperCli.fromDomainToAdapterCli(resultado));

        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
