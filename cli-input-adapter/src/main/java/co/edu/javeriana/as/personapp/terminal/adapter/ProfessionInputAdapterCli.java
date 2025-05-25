package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfessionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfessionMapperCli professionMapperCli;

    private ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void mostrarProfesiones() {
        log.info("Into mostrarProfesiones in Input Adapter");
        professionInputPort.findAll().stream()
            .map(professionMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }

    public void crear() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Código: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        Profession nueva = new Profession(id, nombre, descripcion, null);
        Profession creada = professionInputPort.create(nueva);
        System.out.println("Profesión creada: " + professionMapperCli.fromDomainToAdapterCli(creada));
    }

    public void editar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Código de la profesión a editar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Nueva descripción: ");
        String descripcion = sc.nextLine();

        Profession actualizada = new Profession(id, nombre, descripcion, null);
        try {
            Profession resultado = professionInputPort.edit(id, actualizada);
            System.out.println("Profesión actualizada: " + professionMapperCli.fromDomainToAdapterCli(resultado));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Código de la profesión a eliminar: ");
        int id = sc.nextInt();
        try {
            professionInputPort.drop(id);
            System.out.println("Profesión eliminada.");
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Código de la profesión a buscar: ");
        int id = sc.nextInt();
        try {
            Profession encontrada = professionInputPort.findOne(id);
            System.out.println(professionMapperCli.fromDomainToAdapterCli(encontrada));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
} 
