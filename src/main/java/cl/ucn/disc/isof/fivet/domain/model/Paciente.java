package cl.ucn.disc.isof.fivet.domain.model;

import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Veterinary Patient
 *
 * @author Jose Valdebenito Pertierra
 * @version 20161102
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Paciente extends BaseModel {

    /**
     * File Number
     */
    @Getter
    @Column(nullable = false)
    private Integer numero;

    /**
     * Patient name
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private String nombre;

    /**
     * Birthdate
     */
    @Getter
    @Setter
    @Column
    private Date fechaNacimiento;

    /**
     * Race
     */
    @Getter
    @Setter
    @Column
    private String raza;

    /**
     * Gender
     */
    @Getter
    @Column
    private Sexo sexo;

    /**
     * Color
     */
    @Getter
    @Setter
    private String color;

    /**
     * Control's list
     */
    @Getter
    @ManyToMany
    @OrderBy("fecha")
    private List<Control> Controles;

    /**
     * Add Control to patient
     *
     * @param control
     */
    public void add(final Control control) {

        this.Controles.add(control);

    }

    /**
     * Species
     */
    @Getter
    private String especie;

    /**
     * Gender
     */
    public enum Sexo {
        @EnumValue("Macho")
        MACHO,

        @EnumValue("Hembra")
        HEMBRA,

        @EnumValue("Indeterminado")
        INDETERMINADO,
    }

}
