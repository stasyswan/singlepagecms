package gd.java.lectures.lecture2;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

public interface TestLifecycleLogger {

    static final Logger LOG = Logger.getLogger(TestLifecycleLogger.class.getName());

    @BeforeAll
    static void beforeAllTests() {
        LOG.info("beforeAllTests");
    }

    @AfterAll
    static void afterAllTests() {
        LOG.info("afterAllTests");
    }

    @BeforeEach
    default void beforeEachTest(TestInfo testInfo) {
        LOG.info(() -> String.format("About to execute [%s] \n",
                testInfo.getDisplayName()));
    }

    @AfterEach
    default void afterEachTest(TestInfo testInfo) {
        LOG.info(() -> String.format("Finished executing [%s] \n",
                testInfo.getDisplayName()));
    }

}
