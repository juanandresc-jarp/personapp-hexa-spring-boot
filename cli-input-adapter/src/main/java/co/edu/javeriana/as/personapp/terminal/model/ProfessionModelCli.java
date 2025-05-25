package co.edu.javeriana.as.personapp.terminal.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionModelCli {
    private Integer identification;
    private String name;
    private String description;

}
