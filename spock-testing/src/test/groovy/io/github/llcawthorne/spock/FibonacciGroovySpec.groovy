package io.github.llcawthorne.spock

import spock.lang.*

import static java.util.concurrent.TimeUnit.MILLISECONDS

class FibonacciGroovySpec extends Specification {

    def "Simple test"() {
        setup:
        BadFibonacci f = new BadFibonacci();

        expect:
        f.fibonacci(1) == 1
        f.fibonacci(4) == 3
    }

    def "Simple test with Zero"() {
        setup:
        BadFibonacci f = new BadFibonacci()

        expect:
        f.fibonacci(1) != 0
    }

    def "not assertEquals but Groovy Truth"() {
        expect:
        f2.fibonacci(1)
    }

    @Ignore
    // @IgnoreRest
    def "Simple ignored test"() {
        setup:
        BadFibonacci f = new BadFibonacci()

        expect:
        f.fibonacci(1) == 0
    }

    @FailsWith(MissingMethodException)
    // @FailsWith(SpockAssertionError)
    def "Simple test with Exception"() {
        setup:
        BadFibonacci f = new BadFibonacci()
        Integer i = 0

        when:
        i = null

        then:
        notThrown(f.fibonacci(i))

    }

    def "composed test with string"() {
        setup:
        BadFibonacci f = new BadFibonacci()

        when:
        def temp = f.fibonacci(1) + " " + f.fibonacci(4);

        then:
        temp.contains '1'
        and:
        temp.contains '3'
    }

    @Ignore
    @Timeout(value = 200, unit = MILLISECONDS)
    def "Simple test with TimeOut"() {
        setup:
        BadFibonacci f = new BadFibonacci()

        expect:
        f.fibonacci(100) == 0
    }

    def setup() {}      // run before every feature method
    def cleanup() {}    // run after every feature method
    def setupSpec() {}  // run before the first feature method
    def cleanupSpec() {}    // run after the last feature method

    // instead of all those BadFibonacci f = new BadFibonacci() setup's, we could done this:
    @Subject
    BadFibonacci f2 = new BadFibonacci();
}
