package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_EDITAR = 3;
	private static final int OPCION_ELIMINAR = 4;
	private static final int OPCION_BUSCAR = 5;

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
					case OPCION_REGRESAR_MODULOS:
						isValid = true;
						break;
					case PERSISTENCIA_MARIADB:
						personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					case PERSISTENCIA_MONGODB:
						personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
					case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
						isValid = true;
						break;
					case OPCION_VER_TODO:
						personaInputAdapterCli.historial();
						break;
					case OPCION_CREAR:
						personaInputAdapterCli.crear();
						break;
					case OPCION_EDITAR:
						personaInputAdapterCli.editar();
						break;
					case OPCION_ELIMINAR:
						personaInputAdapterCli.eliminar();
						break;
					case OPCION_BUSCAR:
						personaInputAdapterCli.buscar();
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
				keyboard.nextLine(); // limpiar buffer
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_CREAR + " para crear persona");
		System.out.println(OPCION_EDITAR + " para editar persona");
		System.out.println(OPCION_ELIMINAR + " para eliminar persona");
		System.out.println(OPCION_BUSCAR + " para buscar persona");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			keyboard.nextLine(); // limpiar buffer
			return -1; // opción inválida
		}
	}
}
