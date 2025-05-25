package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

@Mapper
public class EstudiosMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	public EstudiosEntity fromDomainToAdapter(Study study) {
		if (study == null) {
			return null;
		}
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		estudioPK.setCcPer(study.getPerson().getIdentification());
		estudioPK.setIdProf(study.getProfession().getIdentification());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		estudio.setPersona(personaMapperMaria.fromDomainToAdapter(study.getPerson()));
		estudio.setProfesion(profesionMapperMaria.fromDomainToAdapter(study.getProfession()));
		return estudio;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		return new Study(
			personaMapperMaria.fromAdapterToDomainBasic(estudiosEntity.getPersona()),
			profesionMapperMaria.fromAdapterToDomainBasic(estudiosEntity.getProfesion()),
			validateGraduationDate(estudiosEntity.getFecha()),
			validateUniversityName(estudiosEntity.getUniver())
		);
	}

	private LocalDate validateGraduationDate(Date fecha) {
		if (fecha instanceof java.sql.Date) {
			return ((java.sql.Date) fecha).toLocalDate(); // Directamente convierte a LocalDate
		}
		return fecha != null ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}


	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}