package io.github.llcawthorne.junit5.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import static org.assertj.core.api.Assertions.assertThat;

public class RepeatedInvocationTest {

    private static int REPETITION_COUNT = 0;

    @RepeatedTest(5)
    void repeated(RepetitionInfo repetitions) {
        REPETITION_COUNT++;
        // RepetitionInfo::getCurrentReptetition starts with 1
        // NOTE: We can still use AssertJ assertions
        assertThat(repetitions.getCurrentRepetition()).isEqualTo(REPETITION_COUNT);
        assertThat(repetitions.getTotalRepetitions()).isEqualTo(5);
    }

    @DisplayName("Calling repeatedly...")
    @RepeatedTest(value = 5,
        name = "... {currentRepetition}th of {totalRepetitions} times")
    void repeatedWithDisplayName(RepetitionInfo repetitions) { }
}
