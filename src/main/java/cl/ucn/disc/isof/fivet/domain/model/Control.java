package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Veterinary Control
 *
 * @author Jose Valdebenito Pertierra
 * @version 20161026
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Control extends BaseModel{

    /**
     * Fecha
     */
    @Getter
    @Setter
    @NotEmpty
    @Column
    private Date fecha;

    /**
     * Next Control
     */
    @Getter
    @Setter
    @NotEmpty
    private Date proxControl;

    /**
     * Temperature
     */
    @Getter
    @Setter
    private Double temperatura;

    /**
     * Weight
     */
    @Getter
    @Setter
    private Double peso;

    /**
     * Tall
     */
    @Getter
    @Setter
    private Double altura;

    /**
     * Diagnostic
     */
    @Getter
    @Setter
    @NotEmpty
    private String diagnostico;

    /**
     * Note
     */
    @Getter
    @Setter
    private String nota;

    /**
     * Vet
     */
    @Getter
    @Column(nullable = false)
    @ManyToOne
    private Persona veterinario;

}
