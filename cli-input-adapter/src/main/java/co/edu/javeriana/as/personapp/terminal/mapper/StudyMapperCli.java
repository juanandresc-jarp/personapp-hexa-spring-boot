package co.edu.javeriana.as.personapp.terminal.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Component
public class StudyMapperCli {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public StudyModelCli fromDomainToAdapterCli(Study study) {
        return new StudyModelCli(
                study.getPerson().getIdentification(),
                study.getProfession().getIdentification(),
                study.getGraduationDate() != null ? study.getGraduationDate().format(FORMATTER) : "",
                study.getUniversityName() != null ? study.getUniversityName() : ""
        );
    }

    public Study fromAdapterCliToDomain(StudyModelCli cliModel) {
        Person person = new Person(cliModel.getPersonId(), null, null, null);
        Profession profession = new Profession(cliModel.getProfessionId(), null);

        LocalDate graduationDate = parseDate(cliModel.getGraduationDate());

        return new Study(
                person,
                profession,
                graduationDate,
                cliModel.getUniversityName());
    }

    private LocalDate parseDate(String dateString) {
        try {
            return (dateString != null && !dateString.isEmpty()) ? LocalDate.parse(dateString, FORMATTER) : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
