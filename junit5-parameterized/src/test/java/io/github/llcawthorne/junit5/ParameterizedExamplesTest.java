package io.github.llcawthorne.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParameterizedExamplesTest {

    // Simple parameterized values, can be strings, ints, longs, or doubles
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = { "Hello", "JUnit" })
    void withValueSource(String word) {
        assertNotNull(word);
    }

    // We can attache descriptive names for the sub-tests instead of the default "[#{index}] {argument}"
    @org.junit.jupiter.params.ParameterizedTest(name = "run #{index} with [{arguments}]")
    // I could get argument 0 with {0} instead of using {arguments}
    @ValueSource(strings = { "Hello", "JUnit" })
    void withValueSourceAndDescriptiveName(String word) { }

    // DisplayName lets you use a name other than the method name
    // CsvSource lets you do multiple arguments of multiple types
    @DisplayName("Roman Numeral")
    @org.junit.jupiter.params.ParameterizedTest(name = "\"{0}\" should be {1}")
    @CsvSource({ "I, 1", "II, 2", "V, 5" })
    void withNiceName(String word, int number) { }

    // Certain parameters can be injected.  TestInfo and TestReporter
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = { "Hello", "JUnit" })
    void withOtherParams(String word, TestInfo info, TestReporter reporter) {
        reporter.publishEntry(info.getDisplayName(), "Word: " + word);
    }

    // A parameterized test is made up of (1) a method with parameters,
    // (2) the @ParameterizedTest annotation, (3) parameter values
    // We've seen @ValueSource for parameter values
    // as mentioned before, it can be strings, ints, longs, or doubles
    // It is only of use on single parameter test cases
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(longs = { 42, 63 })
    void withValueSource(long number) { }

    // There is also EnumSource which tests for all or part of an Enum's values
    @org.junit.jupiter.params.ParameterizedTest
    @EnumSource(TimeUnit.class)
    void withAllEnumValues(TimeUnit unit) { }

    @org.junit.jupiter.params.ParameterizedTest
    @EnumSource(value = TimeUnit.class,
                names = {"NANOSECONDS", "MICROSECONDS"})
    void withSomeEnumValues(TimeUnit unit) { }

    // @MethodSource is more flexible for multiple params
    @org.junit.jupiter.params.ParameterizedTest
    @MethodSource("createWordsWithLength")
    void withMethodSource(String word, int length) { }

    // You don't have to wrap the return values in Arguments.of, if you're returning a single argument
    private static Stream<Arguments> createWordsWithLength() {
        return Stream.of(
                Arguments.of("Hello", 5),
                Arguments.of("JUnit 5", 7)
        );
    }

    // We've already seen CsvSource
    @org.junit.jupiter.params.ParameterizedTest
    @CsvSource({ "Hello, 5", "JUnit 5, 7", "'Hello, JUnit 5!', 15" })
    void withCsvSource(String word, int length) {
        assertEquals(length, word.length());
    }

    // There's also CsvFileSource for a file in test resources
    @org.junit.jupiter.params.ParameterizedTest
    @CsvFileSource(resources = "/word-lengths.csv")
    void withCsvFileSource(String word, int length) {
        assertEquals(length, word.length());
    }

    // Sometimes you need more complicated arguments than strings, enums, or primitives
    // Then you need an Argument Converter
    @org.junit.jupiter.params.ParameterizedTest
    @CsvSource({ "s, true, 3.14159265359, JUNE, 2017, 2017-06-21T22:00:00" })
    void testDefaultConverters(char l, boolean b, double d, Summer s, Year y, LocalDateTime dt) { }

    enum Summer { JUNE, JULY, AUGUST, SEPTEMBER; }

    // If our dates aren't ISO, we can provide a @JavaTimeConversoinPattern
    @org.junit.jupiter.params.ParameterizedTest
    @CsvSource({ "23.08.2018", "01.07.2019" })
    void testDateConversionWithPattern(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate dt) { }

    // This small test requires a lot of setup below
    @org.junit.jupiter.params.ParameterizedTest
    @CsvSource({ "(0/0), 0", "(0/1), 1", "(1/1), 1.414" })
    void convertPointNorm(@ConvertPoint Point point, double norm) {
        assertEquals(norm, point.norm(), 0.001);
    }

    // If we didn't make the customer annotation, we could've just used @ConvertWith
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = { "(0/0)", "(0/1)", "(1/1)" })
    void convertPointConvertWith(@ConvertWith(PointConverter.class) Point point) { }

    // Without the @ConvertPoint annotation, JUnit follows a couple of sane rules
    // (1) If a type has a single non-private, static method that accepts a String and
    //     returns an instance of itself, JUnit uses this factory method to convert
    //     strings to instances.
    // (2) If there are zero or more than one factory method, JUnit settles for a
    //     factory constructor, which must be non-private and accept a String.

    // So this will use the static Point.from method
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = { "(0/0)", "(0/1)", "(1/1)" })
    void convertPoint(Point point) { }

    // and this will use the String constructor of PointConstructor
    // I'll probably just use the String constructor method most of the time, unless it doesn't make sense...
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = { "(0/0)", "(0/1)", "(1/1)" })
    void convertPointConstructor(PointConstructor point) { }

    @Target({ ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    @ConvertWith(PointConverter.class)
    @interface ConvertPoint { }

    // The PointConverter is really useful when you can't edit the class to add a from or String constructor
    // You always need to implement the ArgumentConverter interface in a converter
    // We wouldn't need this for Point, since it has a static from method to use
    static class PointConverter implements ArgumentConverter {
        @Override
        public Object convert(Object input, ParameterContext parameterContext) throws ArgumentConversionException {
            if (input instanceof Point)
                return input;
            if (input instanceof String)
                try {
                    return Point.from((String) input);
                } catch (NumberFormatException e) {
                    String message = input + " is no correct string representation of a point.";
                    throw new ArgumentConversionException(message, e);
                }
            throw new ArgumentConversionException(input + " is no valid point");
        }
    }

    static class Point {
        final double x, y;

        private Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        static Point from(double x, double y) {
            return new Point(x, y);
        }

        // if we didn't have from(String xy) below, then it would use Point(String xy) constructor

        static Point from(String x, String y) {
            return new Point(Double.parseDouble(x), Double.parseDouble(y));
        }

        static Point from(String xy) {
            if (!xy.startsWith("(") || !xy.endsWith(")"))
                throw new NumberFormatException(xy + " is no valid point");
            String[] x_y = xy.substring(1, xy.length() - 1).trim().split("/");
            if (x_y.length != 2)
                throw new NumberFormatException(xy + " is no valid point");
            return from(x_y[0].trim(), x_y[1].trim());
        }

        @Override
        public String toString() {
            return "(" + x + " / " + y + ")";
        }

        public double norm() {
            return Math.sqrt(x * x + y * y);
        }
    }

    static class PointConstructor {
        final double x, y;

        public PointConstructor(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public PointConstructor(String xy) {
            if (!xy.startsWith("(") || !xy.endsWith(")"))
                throw new NumberFormatException(xy + " is no valid point");
            String[] x_y = xy.substring(1, xy.length() - 1).trim().split("/");
            if (x_y.length != 2)
                throw new NumberFormatException(xy + " is no valid point");
            this.x = Double.parseDouble(x_y[0].trim());
            this.y = Double.parseDouble(x_y[1].trim());
        }

        @Override
        public String toString() {
            return "(" + x + " / " + y + ")";
        }

        public double norm() {
            return Math.sqrt(x * x + y * y);
        }
    }

    // Whew!  This class is big enough, but there's only a couple more small examples
    // What if your input format is weird and you can't map 1 csv field to one parameter
    // Then you need to use ArgumentsAccessor or ArgumentsAggregator
    @org.junit.jupiter.params.ParameterizedTest
    // pretend this is a csv file we don't have strict control over
    // here we have norm, then x, y, but we need xy to make a point with the factories
    @CsvSource({ "0, 0, 0", "1, 0, 1", "1.414, 1, 1" })
    void testPointNorm(double norm, ArgumentsAccessor arguments) {
        Point point = Point.from(arguments.getDouble(1), arguments.getDouble(2));
        assertEquals(norm, point.norm(), 0.001);
    }

    @org.junit.jupiter.params.ParameterizedTest
    @CsvSource({ "0, 0, 0", "1, 0, 1", "1.414, 1, 1" })
    void testPointNormAggregator(double norm, @AggregateWith(PointAggregator.class) Point point) {
        assertEquals(norm, point.norm(), 0.001);
    }

    static class PointAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor arguments, ParameterContext context)
            throws ArgumentsAggregationException {
            return Point.from(arguments.getDouble(1), arguments.getDouble(2));
        }
    }
}
