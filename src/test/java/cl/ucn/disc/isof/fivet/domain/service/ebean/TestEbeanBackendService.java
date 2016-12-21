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
