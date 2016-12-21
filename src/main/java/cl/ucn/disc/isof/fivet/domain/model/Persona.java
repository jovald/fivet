package cl.ucn.disc.isof.fivet.domain.model;

import com.avaje.ebean.annotation.Encrypted;
import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Only a Person.
 *
 * @author Jose Valdebenito Pertierra
 * @version 20161026
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Persona extends BaseModel {

    /**
     * Rut
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String rut;

    /**
     * Complete Name
     */
    @Getter
    @Setter
    @NotEmpty(message = "El nombre no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    /**
     * Password
     */
    @NotEmpty
    @Getter
    @Encrypted
    @Column(nullable = false)
    private String password;

    /**
     * Address
     */
    @NotEmpty
    @Getter
    @Setter
    private String direccion;

    /**
     * Mail
     */
    @Getter
    @Setter
    @Column
    @NotEmpty
    private String mail;

    /**
     * Phone number
     */
    @NotEmpty
    @Getter
    @Setter
    private String telFijo;

    /**
     * Cell phone number
     */
    @NotEmpty
    @Getter
    @Setter
    private String telMovil;

    /**
     * Person's type
     */
    @Getter
    @NotNull
    private Tipo tipo;

    /**
     * Pacients' list
     */
    @Getter
    @ManyToMany
    @OrderBy("numero")
    private List<Paciente> pacientes;

    /**
     * Add Pacient.
     *
     * @param paciente
     */
    public void add(final Paciente paciente) {

        this.pacientes.add(paciente);
    }

    /**
     * Kind of rol
     */
    public enum Tipo {
        @EnumValue("Cliente")
        CLIENTE,

        @EnumValue("Veterinario")
        VETERINARIO,
    }


}
