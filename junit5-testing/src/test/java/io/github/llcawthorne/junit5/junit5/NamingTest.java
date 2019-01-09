package io.github.llcawthorne.junit5.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("This is the Naming Test (class-level name)")
public class NamingTest {

    @Test
    @DisplayName("Assert that true is true (method-level name)")
    void test() {
        assertTrue(true);
    }
}
