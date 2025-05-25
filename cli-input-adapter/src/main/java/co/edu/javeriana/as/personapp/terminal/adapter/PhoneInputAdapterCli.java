package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.PhoneMapperCli;
import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;


import lombok.extern.slf4j.Slf4j;
@Slf4j
@Adapter
public class PhoneInputAdapterCli {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private PhoneMapperCli phoneMapperCli;

    private PhoneInputPort phoneInputPort;
    private PersonInputPort personInputPort;

    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial PhoneEntity in Input Adapter");
        phoneInputPort.findAll().stream()
            .map(phoneMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }

    public void crear() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Número de teléfono: ");
        String numero = sc.nextLine();
        System.out.print("Operador: ");
        String operador = sc.nextLine();
        System.out.print("Cédula del propietario: ");
        int cc = sc.nextInt();

        try {
            Person owner = personInputPort.findOne(cc);
            Phone nuevo = new Phone(numero, operador, owner);
            Phone creado = phoneInputPort.create(nuevo);
            System.out.println("Teléfono creado: " + phoneMapperCli.fromDomainToAdapterCli(creado));
        } catch (NoExistException e) {
            System.out.println("Error: no se encontró el propietario con cédula " + cc);
        }
    }

    public void editar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Número de teléfono a editar: ");
        String numero = sc.nextLine();
        System.out.print("Nuevo operador: ");
        String operador = sc.nextLine();
        System.out.print("Nueva cédula del propietario: ");
        int cc = sc.nextInt();

        try {
            Person owner = personInputPort.findOne(cc);
            Phone actualizado = new Phone(numero, operador, owner);
            Phone resultado = phoneInputPort.edit(numero, actualizado);
            System.out.println("Teléfono actualizado: " + phoneMapperCli.fromDomainToAdapterCli(resultado));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Número de teléfono a eliminar: ");
        String numero = sc.nextLine();
        try {
            phoneInputPort.drop(numero);
            System.out.println("Teléfono eliminado.");
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Número de teléfono a buscar: ");
        String numero = sc.nextLine();
        try {
            Phone encontrado = phoneInputPort.findOne(numero);
            System.out.println(phoneMapperCli.fromDomainToAdapterCli(encontrado));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

