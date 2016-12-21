package cl.ucn.disc.isof.fivet.domain.service;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;

import java.util.List;

/**
 * Interface that presents the system operations.
 */
public interface BackendService {

    /**
     * Get a person from his Rut or Nombre
     *
     * @param rutEmail
     * @return the Persona
     */

    /**
     *
     * @param rutEmail
     * @return
     */
    Persona getPersona(final String rutEmail);

    /**
     * Get a list with the Pacientes
     *
     * @return the {@link List} of {@link Paciente}
     */
    List<Paciente> getPacientes();

    /**
     * Get a {@link Paciente} from Numero de Ficha.
     *
     * @param numeroPaciente of Ficha.
     * @return the {@link Paciente}.
     */
    Paciente getPaciente(final Integer numeroPaciente);

    /**
     * Get all Controles of a Veterinario.
     *
     * @param rutVeterinario who realized the Control.
     * @return the {@link List} of {@link Control}.
     */
    List<Control> getControlesVeterinario(final String rutVeterinario);

    /**
     * Get all {@link Paciente} matching name.
     *
     * @param nombre e.g.: "pep" can return pepe, pepa, pepilla, etc..
     * @return the {@link List} of {@link Paciente}.
     */
    List<Paciente> getPacientesPorNombre(final String nombre);

    /**
     * Add a {@link Control} to {@link Paciente} identify by numeroPaciente.
     *
     * @param control        to add.
     * @param numeroPaciente to asociate.
     * @throws RuntimeException en caso de no encontrar al paciente.
     */
    void agregarControl(final Control control, final Integer numeroPaciente);

    /**
     * Backend Initialization.
     */
    void initialize();

    /**
     * Backend Close.
     */
    void shutdown();

}
