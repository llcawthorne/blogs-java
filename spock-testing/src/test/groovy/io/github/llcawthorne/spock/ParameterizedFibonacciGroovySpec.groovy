package io.github.llcawthorne.spock

import spock.lang.*

class ParametrizedFibonacciGroovySpec extends Specification {

    def "parametrized test"() {
        setup:
        BadFibonacci f = new BadFibonacci()

        expect:
        f.fibonacci(index) == fibonacciNumber

        where:
        index || fibonacciNumber
        1     || 1
        2     || 1
        3     || 2
        6     || 8
    }

}
