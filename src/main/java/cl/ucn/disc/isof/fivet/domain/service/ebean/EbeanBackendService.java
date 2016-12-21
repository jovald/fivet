package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Expr;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.ServerConfig;
import com.durrutia.ebean.BaseModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Backend Implementation ad BD connection.
 *
 * @author Jose Valdebenito Pertierra
 * @version 20161026
 */
@Slf4j
public class EbeanBackendService implements BackendService {

    /**
     * EBean server
     */
    private final EbeanServer ebeanServer;

    public EbeanBackendService(final String database) {

        log.debug("Loading EbeanBackend in database: {}", database);

        /**
         * Configuration
         */
        ServerConfig config = new ServerConfig();
        config.setName(database);
        config.setDefaultServer(true);
        config.loadFromProperties();

        // Don't try this at home
        //config.setAutoCommitMode(false);

        // config.addPackage("package.de.la.clase.a.agregar.en.el.modelo");
        config.addClass(BaseModel.class);

        config.addClass(Persona.class);
        config.addClass(Persona.Tipo.class);

        config.addClass(Paciente.class);
        config.addClass(Paciente.Sexo.class);

        config.addClass(Control.class);


        // http://ebean-orm.github.io/docs/query/autotune
        config.getAutoTuneConfig().setProfiling(false);
        config.getAutoTuneConfig().setQueryTuning(false);

        config.setEncryptKeyManager(new EncryptKeyManager() {

            @Override
            public void initialise() {
                log.debug("Initializing EncryptKey ..");
            }

            @Override
            public EncryptKey getEncryptKey(final String tableName, final String columnName) {

                log.debug("gettingEncryptKey for {} in {}.", columnName, tableName);

                // Return the encrypt key
                return () -> tableName + columnName;
            }
        });

        this.ebeanServer = EbeanServerFactory.create(config);

        log.debug("EBeanServer ready to go.");

    }

    /**
     * Get Persona form his mail or Rut.
     * @param rutEmail Rut or mail of Persona
     * @return Persona
     */
    @Override
    public Persona getPersona(String rutEmail) {
        return this.ebeanServer.find(Persona.class)
                .where().or(
                        Expr.eq("rut",rutEmail),
                        Expr.eq("mail", rutEmail)
                ).findUnique();
    }

    /**
     * Get all Pacientes of the system.
     * @return List with Pacientes.
     */
    @Override
    public List<Paciente> getPacientes() {
        return this.ebeanServer.find(Paciente.class).findList();
    }

    /**
     * Get Paciente from his unique number.
     * @param numeroPaciente
     * @return Paciente
     */
    @Override
    public Paciente getPaciente(Integer numeroPaciente) {
        return this.ebeanServer.find(Paciente.class).where(
                Expr.eq("numero", numeroPaciente)
        ).findUnique();
    }

    /**
     * Get all Controles of a Veterinario.
     * @param rutVeterinario who relized the Control.
     * @return List with Veterinarios.
     */
    @Override
    public List<Control> getControlesVeterinario(String rutVeterinario) {
        return this.ebeanServer.find(Control.class).where()
                .eq("veterinario.rut",rutVeterinario)
                .findList();
    }

    /**
     * Get Paciente from Nombre matching.
     * @param nombre e.g.: "pep" can return pepe, pepa, pepilla, etc..
     * @return List with Pacientes
     */
    @Override
    public List<Paciente> getPacientesPorNombre(String nombre) {

        return ebeanServer.find(Paciente.class).where()
                .eq("nombre",nombre)
                .findList();
    }

    
