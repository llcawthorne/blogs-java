package io.github.llcawthorne.spock;

public class BadFibonacci {

    // FIXME: very bad performance
    private static int fibonacci(int n) {
        if (n <= 1)
            return n;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        System.out.println("Result: " + fibonacci(4));
    }
}
