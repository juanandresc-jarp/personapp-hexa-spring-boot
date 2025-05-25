package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.Scanner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	public void crear() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Cédula: ");
		int cc = sc.nextInt();
		sc.nextLine();
		System.out.print("Nombre: ");
		String nombre = sc.nextLine();
		System.out.print("Apellido: ");
		String apellido = sc.nextLine();
		System.out.print("Género (MALE/FEMALE/OTHER): ");
		String genero = sc.nextLine();
		System.out.print("Edad: ");
		int edad = sc.nextInt();

		Person nueva = new Person(cc, nombre, apellido, Gender.valueOf(genero.toUpperCase()), edad, null, null);
		Person creada = personInputPort.create(nueva);
		System.out.println("Persona creada: " + personaMapperCli.fromDomainToAdapterCli(creada));
	}

	public void editar() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Cédula de la persona a editar: ");
		int cc = sc.nextInt();
		sc.nextLine();
		System.out.print("Nuevo nombre: ");
		String nombre = sc.nextLine();
		System.out.print("Nuevo apellido: ");
		String apellido = sc.nextLine();
		System.out.print("Nuevo género (M/F): ");
		String genero = sc.nextLine();
		System.out.print("Nueva edad: ");
		int edad = sc.nextInt();

		Person actualizada = new Person(cc, nombre, apellido, Gender.valueOf(genero.toUpperCase()), edad, null, null);
		try {
			Person resultado = personInputPort.edit(cc, actualizada);
			System.out.println("Persona actualizada: " + personaMapperCli.fromDomainToAdapterCli(resultado));
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}


	public void eliminar() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Cedula de la persona a eliminar: ");
		int cc = sc.nextInt();
		try {
			personInputPort.drop(cc);
			System.out.println("Persona eliminada.");
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void buscar() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Cedula de la persona a buscar: ");
		int cc = sc.nextInt();
		try {
			Person encontrada = personInputPort.findOne(cc);
			System.out.println(personaMapperCli.fromDomainToAdapterCli(encontrada));
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
