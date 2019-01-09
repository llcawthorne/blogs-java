package io.github.llcawthorne.junit5.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// Basics of the lifecycle haven't really changed
public class LifecycleTest {

    @BeforeAll
    static void initiializeExternalResources() {
        System.out.println("Initializing external resources...");
    }

    @BeforeEach
    void initializeMockObjects() {
        System.out.println("Initializing mock objects...");
    }

    @Test
    void someTest() {
        System.out.println("Running some test...");
    }

    @Test
    void otherTest() {
        assumeTrue(true);  // Assumptions failing cause a test to be aborted, but it doesn't fail

        System.out.println("Running another test...");
        assertNotEquals(1, 42, "Why would these be the same?");
    }

    @Test
    @Disabled
    void disabledTest() {
        System.exit(1);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void disabledOnWindowsTest() {
        fail("Only runs on Unix/Linux or Mac");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Tearing down...");
    }

    @AfterAll
    static void freeExternalResources() {
        System.out.println("Freeing external resources...");
    }
}
