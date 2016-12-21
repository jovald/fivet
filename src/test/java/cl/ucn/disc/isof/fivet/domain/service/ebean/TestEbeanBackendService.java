package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.List;

/**
 * Testing Class {@link BackendService}.
 */
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class TestEbeanBackendService {

    /**
     * All test must finish before 120s.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(120);

    /**
     * BD Configuration:  h2, hsql, sqlite.
     */
    private static final String DB = "sqlite";

    /**
     * Backend.
     */
    private BackendService backendService;

    /**
     * Timer.
     */
    private Stopwatch stopWatch;

    /**
     * Before any test.
     */
    @Before
    public void beforeTest() {
        stopWatch = Stopwatch.createStarted();
        log.debug("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * After any test.
     */
    @After
    public void afterTest() {
        log.debug("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.debug("Test finished in {}", stopWatch.toString());
    }

    /**
     * Test de la persona
     */
    @Test
    public void testPersona() {

        final String rut = "1-1";
        final String nombre = "Este es mi nombre";
        final String mail = "estoEsUnEmail@email.cl";

        // Insert into backend
        {
            final Persona persona = Persona.builder()
                    .nombre(nombre)
                    .rut(rut)
                    .mail(mail)
                    .password("durrutia123")
                    .tipo(Persona.Tipo.CLIENTE)
                    .build();

            persona.insert();

            log.debug("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
            Assert.assertEquals("Nombre distintos!", nombre, persona.getNombre());
            Assert.assertNotNull("Pacientes null", persona.getPacientes());
            Assert.assertTrue("Pacientes != 0", persona.getPacientes().size() == 0);

            // Update nombre
            persona.setNombre(nombre + nombre);
            persona.update();
        }

        // Get from backend v2
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona found: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre + nombre, persona.getNombre());
        }

        // Get from backend v3
        {
            final Persona persona = backendService.getPersona(mail);
            log.debug("Persona encontrada: {}", persona);
            Assert.assertNotNull("No se encontro a la persona", persona);
            Assert.assertEquals("Nombre diferente", nombre + nombre, persona.getNombre());
            log.debug("Pruebas de obtencion de persona lista");
        }

        // Get from backend v4
        {
            // se busca una persona que no exista en el sistema
            final Persona persona = backendService.getPersona("11-9");
            log.debug("Buscando una persona inexistente");
            Assert.assertEquals("Se encontro una persona", null, persona);

        }

    }


    /**
     * EbeanBackendService.getPacientes() Testing.
     */
    @Test
    public void testPersonaPaciente() {

        log.debug("Testing EbeanBackendService.getPacientes() ..");

        final Persona persona = Persona.builder()
                .nombre("Juanito")
                .rut("2-7")
                .password("123")
                .tipo(Persona.Tipo.CLIENTE)
                .build();

        persona.insert();

        log.debug("Persona inserted: {}", persona);
        Assert.assertNotNull("Objeto sin id", persona.getId());

        // Create a new Paciente
        String patientName1 = "laika";
        Integer patientNumber = 123;
        final Paciente patient1 = Paciente.builder()
                .nombre(patientName1)
                .numero(patientNumber)
                .build();
        patient1.insert();

        // Create a new Paciente
        String patientName2 = "damian";
        Integer patientNumber2 = 124;
        final Paciente patient2 = Paciente.builder()
                .nombre(patientName2)
                .numero(patientNumber2)
                .build();
        patient2.insert();

        // Create a new Paciente
        String patientName3 = "sort";
        Integer patientNumber3 = 500;
        final Paciente patient3 = Paciente.builder()
                .nombre(patientName3)
                .numero(patientNumber3)
                .build();
        patient3.insert();

        // Add Pacientes to List of pacientes of Persona
        persona.add(patient1);
        persona.add(patient2);
        persona.add(patient3);
        persona.update();
        log.debug("Pacientes added");

        List<Paciente> patients = backendService.getPacientes();
        Assert.assertNotNull("List not null", patients);
        Assert.assertEquals("error in Pacientes quantity", patients.size(), 3);

        for (Paciente patient : patients){
            Assert.assertNotNull("Paciente without Nombre", patient.getNombre());
            Assert.assertNotNull("Paciente without Numero", patient.getNumero());
        }

        log.debug("Test EbeanBackendService.getPacientes() OK");
    }
