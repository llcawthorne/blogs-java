package io.github.llcawthorne.junit5.junit5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertTrue;

// We could also put @Disabled on this class
public class DisableTest {

    @Test
    @Disabled
    void failingTest() {
        assertTrue(false);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    @DisabledOnJre(JRE.JAVA_10)
    void disabledInJava10() {
        assertTrue(true);
    }
}
