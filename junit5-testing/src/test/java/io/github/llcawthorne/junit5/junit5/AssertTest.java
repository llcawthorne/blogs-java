package io.github.llcawthorne.junit5.junit5;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static java.time.Duration.of;
import static java.time.Duration.ofMinutes;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class AssertTest {

    @Test
    void assertWithBoolean_pass() {
        assertTrue(true);
        assertTrue(this::truism);

        assertFalse(false, () -> "Really " + "expensive " + "message" + ".");
    }

    private boolean truism() {
        return true;
    }

    @Test
    void assertWithComparison_pass() {
        List<String> expected = asList("element");
        List<String> actual = new LinkedList<>(expected);

        assertEquals(expected, actual);
        assertEquals(expected, actual, "Different 'List' implementations should be equal.");
        assertEquals(expected, actual, () -> "Different 'List' implementations should be equal>");

        assertNotSame(expected, actual, "Obviously not the same instance.");
    }

    @Test
    void exceptionTesting() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("a message");
        });
        assertEquals("a message", exception.getMessage());
    }

    @Test
    void timeoutNotExceeded() {
        // The following assertion succeeds.
        assertTimeout(ofMinutes(2), () -> {
            // Perform task that takes less than 2 minutes.
        });
    }

    @Test
    void failTheTest_fails() {
        fail("epicly");
    }

    // We can use assertAll to get the expected and actual comparison displayed for more than
    // one comparison at a time.  It doesn't stop with the first failure like it would for
    // multiple assertions.
    @Test
    void assertAllProperties_fails() {
        Address address = new Address("New City", "Some Street", "No");

        assertAll("address",
                () -> assertEquals("Neustadt", address.city),
                () -> assertEquals("Irgendeinestraße", address.street),
                () -> assertEquals("Nr", address.number)
        );
    }

    static class Address {
        final String city;
        final String street;
        final String number;

        private Address(String city, String street, String number) {
            this.city = city;
            this.street = street;
            this.number = number;
        }
    }

    @Test
    void assertTimeout_runsLate_failsButFinishes() {
        assertTimeout(of(100, MILLIS), () -> {
            sleepUninterrupted(250);
            // you will see this message
            System.out.println("Woke up");
        });
    }

    @Test
    void assertTimeoutPreemptively_runsLate_failsAndAborted() {
        assertTimeoutPreemptively(of(100, MILLIS), () -> {
            sleepUninterrupted(250);
            // you shouldn't see this message, but I still do...
            System.out.println("Woke up when I wasn't supposed to");
        });
    }

    private static void sleepUninterrupted(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) { }
    }
}
