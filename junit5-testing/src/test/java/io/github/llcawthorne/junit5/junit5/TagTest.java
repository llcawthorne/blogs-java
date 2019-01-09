package io.github.llcawthorne.junit5.junit5;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TagTest {

    @Test
    @Tag("database")
    void database() {
        fail("This test will be execute in database suite and all tests");
    }
}
