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
import sun.rmi.runtime.Log;

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
        log.info("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * After any test.
     */
    @After
    public void afterTest() {
        log.info("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.info("Test finished in {}", stopWatch.toString());
    }

    /**
     * EbeanBackendService.getPersona(string rut) Testing.
     */
    @Test
    public void getPersonaTest() {

        log.info("Testing EbeanBackendService.getPersona() ..");

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

            log.info("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.info("Persona founded: {}", persona);
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
            log.info("Persona found: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre + nombre, persona.getNombre());
        }

        // Get from backend v3
        {
            final Persona persona = backendService.getPersona(mail);
            log.info("Persona encontrada: {}", persona);
            Assert.assertNotNull("No se encontro a la persona", persona);
            Assert.assertEquals("Nombre diferente", nombre + nombre, persona.getNombre());
            log.info("Pruebas de obtencion de persona lista");
        }

        // Get from backend v4
        {
            // se busca una persona que no exista en el sistema
            final Persona persona = backendService.getPersona("11-9");
            log.info("Buscando una persona inexistente");
            Assert.assertEquals("Se encontro una persona", null, persona);

        }

        log.info("Test EbeanBackendService.getPersona() OK");

    }


    /**
     * EbeanBackendService.getPacientes() Testing.
     */
    @Test
    public void getPacientesTest() {

        log.info("Testing EbeanBackendService.getPacientes() ..");

        final Persona persona = Persona.builder()
                .nombre("Juanito")
                .rut("2-7")
                .password("123")
                .tipo(Persona.Tipo.CLIENTE)
                .build();

        persona.insert();

        log.info("Persona inserted: {}", persona);
        Assert.assertNotNull("Without ID", persona.getId());

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
        log.info("Pacientes added");

        List<Paciente> patients = backendService.getPacientes();
        Assert.assertNotNull("List not null", patients);
        Assert.assertEquals("error in Pacientes quantity", patients.size(), 3);

        for (Paciente patient : patients){
            Assert.assertNotNull("Paciente without Nombre", patient.getNombre());
            Assert.assertNotNull("Paciente without Numero", patient.getNumero());
        }

        log.info("Test EbeanBackendService.getPacientes() OK");
    }

    /**
     * EbeanBackendService.getPaciente(Integer numeroPaciente) Testing.
     */
    @Test
    public void getPacienteTest(){
        log.info("Testing EbeanBackendService.getPaciente(Integer numeroPaciente) ..");

        // Non-Existence Test
        Assert.assertNull(backendService.getPaciente(5124723));

        final Integer patientNumber = 5124723;
        final String patientName = "laika";
        final String patientColor = "negrusho";
        final String patientSpecies= "NN";
        final String patientRace = "NN";
        final Paciente.Sexo patientGender = Paciente.Sexo.MACHO;

        final Paciente patient = Paciente.builder()
                .numero(patientNumber)
                .nombre(patientName)
                .color(patientColor)
                .especie(patientSpecies)
                .raza(patientRace)
                .sexo(patientGender)
                .build();
        patient.insert();
        log.info("Paciente inserted: {}", patient);

        Paciente patientBack = backendService.getPaciente(patientNumber);

        // Existence Test
        Assert.assertNotNull("Paciente null", patientBack);

        // Atribute data Test
        Assert.assertEquals("Different Nombre", patientName, patientBack.getNombre());
        Assert.assertEquals("Different Numero", patientNumber,patientBack.getNumero());
        Assert.assertEquals("Different Color", patientColor,patientBack.getColor());
        Assert.assertEquals("Different Especie", patientSpecies,patientBack.getEspecie());
        Assert.assertEquals("Different Raza", patientRace,patientBack.getRaza());
        Assert.assertEquals("Different Sexo", patientGender,patientBack.getSexo());

        log.info("Test EbeanBackendService.getPaciente(Integer numeroPaciente) OK");
    }

    /**
     * EbeanBackendService.getControlesVeterinario(string rutVeterinario) Testing
     */
    @Test
    public void getControlesVeterinarioTest(){
        log.info("Testing EbeanBackendService.getControlesVeterinario(string rutVeterinario) Testing ..");

        // Persona Creation (Veterinario)
        final String vetRut = "2-7";
        final Persona vet = Persona.builder()
                .nombre("Pinocheque")
                .rut(vetRut)
                .password("123456")
                .direccion("Cualquier lado")
                .mail("dinero@mas.dinero.org")
                .telFijo("123-llame ya")
                .tipo(Persona.Tipo.VETERINARIO)
                .build();
        vet.insert();

        log.info("Veterinario inserted: {}", vet);

        // Person Creation (Cliente)
        final Persona person = Persona.builder()
                .nombre("Test")
                .rut("2-7")
                .password("test123")
                .tipo(Persona.Tipo.CLIENTE)
                .build();
        person.insert();

        log.info("Persona inserted: {}", person);

        // Paciente Creation
        final Paciente patient = Paciente.builder()
                .numero(65432)
                .nombre("Laika")
                .sexo(Paciente.Sexo.INDETERMINADO)
                .especie("Perruno")
                .raza("NN")
                .build();
        patient.insert();

        log.info("Paciente inserted: {}", patient);

        // Non-existence Test
        Assert.assertTrue(backendService.getControlesVeterinario(vetRut).size() == 0);

        // Control Creation
        Date date1 = new Date(2014,10,1);
        Date date2 = new Date(2014,11,2);
        final Control control = Control.builder()
                .veterinario(vet)
                .fecha(date1)
                .proxControl(date2)
                .diagnostico("Sano")
                .build();
        control.insert();
        log.info("Control inserted: {}", control);

        // Control Creation
        Date date3 = new Date(2015,12,1);
        final Control control2 = Control.builder()
                .veterinario(vet)
                .fecha(date2)
                .proxControl(date3)
                .diagnostico("Sano")
                .build();
        control2.insert();
        log.info("Control inserted: {}", control2);

        // Update Controls of Paciente
        patient.add(control);
        patient.add(control2);
        patient.update();

        // Add Paciente to Person (cliente)
        person.add(patient);
        person.update();

        // Getting Controls of Person (veterinario)
        List<Control> controlesBack =  backendService.getControlesVeterinario(vetRut);

        // Existence Test
        Assert.assertNotNull(controlesBack);

        // Quantity Test
        Assert.assertEquals("Controls Quantity", 2, controlesBack.size());

        for(Control controlB : controlesBack){
            // Atribute data test
            Assert.assertNotNull("At least a Veterinario", controlB.getVeterinario());

            // Inverse Navigation Test
            Assert.assertEquals("Different Ruts", vetRut, controlB.getVeterinario().getRut());
        }

        log.info("Test EbeanBackendService.getControlesVeterinario(string rutVeterinario) OK");
    }

    /**
     * EbeanBackendService.getPacientesPorNombre(String nombre) Testing
     */
    @Test
    public void getPacientesPorNombreTest(){
        log.info("Testing EbeanBackendService.getPacientesPorNombre(String nombre) ..");

        final String patientName = "tobi";

        // Pacientes Creation
        Paciente patient1 = Paciente.builder()
                .numero(123)
                .nombre(patientName)
                .build();
        patient1.insert();
        log.info("Paciente inserted: {}", patient1);

        Paciente patient2 = Paciente.builder()
                .numero(232)
                .nombre(patientName)
                .build();
        patient2.insert();
        log.info("Paciente inserted: {}", patient2);

        // Getting List of Pacientes
        List<Paciente> patients = backendService.getPacientesPorNombre(patientName);

        // Exsistence Test
        Assert.assertNotNull("Cannot be Null", patients);

        // Quantity Validation
        Assert.assertEquals("Quantity of Pacientes", 2 ,patients.size());

        for(Paciente ptn : patients){
            // Atribute Data validation
            Assert.assertNotNull("nombre no puede ser null", ptn.getNombre());
        }

        //se busca un paciente que no exista
        List<Paciente> pacientesnull = backendService.getPacientesPorNombre("Alfredo");

        // Non-Existence test
        Assert.assertEquals("Non-Existence", backendService.getPacientesPorNombre("Alfredo").size(), 0);

        log.info("Test EbeanBackendService.getPacientesPorNombre(String nombre) OK");
    }

    /**
     * EbeanBackendService.agregarControl(Control control, Integer numeroPaciente) Testing
     */
    @Test
    public void testAgregarControl(){
        log.info("Testing EbeanBackendService.agregarControl(Control control, Integer numeroPaciente) ..");

        final Integer patientNumber = 12345;

        // Paciente creation
        Paciente patient = Paciente.builder()
                .nombre("Laika")
                .numero(patientNumber)
                .build();
        patient.insert();
        log.info("Paciente inserted: {}", patient);

        // Persona creation
        Persona vet = Persona.builder()
                .nombre("Luchito Jarra")
                .rut("1-9")
                .password("1234")
                .tipo(Persona.Tipo.VETERINARIO)
                .build();
        vet.insert();
        log.info("Veterinario inserted: {}", vet);

        // Control creation
        Date fecha = new Date(1993,10,9);
        Control control = Control.builder()
                .veterinario(vet)
                .fecha(fecha)
                .proxControl(fecha)
                .diagnostico("nada")
                .build();
        control.insert();
        log.info("Control inserted: {}", control);

        // Adding Control to Paciente
        backendService.agregarControl(control,patientNumber);
        log.info("Control Added to Paciente");

        // Getting Paciente
        Paciente patientBack = backendService.getPaciente(patientNumber);

        // Existence test
        Assert.assertNotNull("Paciente cannot be null", patientBack);

        // Atributes data validation
        Assert.assertEquals("Different numbers", patientNumber, patientBack.getNumero());
        Assert.assertNotNull("Controles cannot be null", patientBack.getControles());

        for(Control ctrl : patientBack.getControles()){
            Assert.assertEquals("Different Fechas", fecha, ctrl.getFecha());
            Assert.assertEquals("Different Proximo Control", fecha, ctrl.getProxControl());
            Assert.assertEquals("Different Veterinarios", vet, ctrl.getVeterinario());
        }

        // Non-exsistence Test
        Assert.assertNull(backendService.getPaciente(-1));

        log.info("Test EbeanBackendService.agregarControl(Control control, Integer numeroPaciente) OK");
    }

}
