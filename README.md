Java 8 lambdas tutorial
=======================

The goal of this tutorial is to introduce the lambdas in Java 8, also known as [JSR 335 - Lambda Expressions for the JavaTM Programming Language](http://jcp.org/en/jsr/detail?id=335).

The tutorial is based on the version ["Feature Complete"](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-June/002686.html) of Java 8 (b94) and can be downloaded [here](https://jdk8.java.net/download.html).


## How the tutorial works ? ##

The tutorial consists in a testing suite **org.mdumas.lambdas.Lambdas**. Each test need to be passed to complete the tutorial. 

Several tests are a comparison between the current of Java 1.6 / 1.7 syntax to emulate a kind of functionnal programming with the help of the [Guava](https://code.google.com/p/guava-libraries) library and the new one of Java 8.



## What's a lambda expression ##

According to Wikipedia, a [lambda expression](http://en.wikipedia.org/wiki/Lambda_(programming)) is an anonymous function. Based on the work of [Alonzo Church](http://en.wikipedia.org/wiki/Alonzo_Church) on his [lambda calculus](http://en.wikipedia.org/wiki/Lambda_calculus) and used  for the first time in a programming language with [Lisp](http://en.wikipedia.org/wiki/Lisp_(programming_language)), lambda expressions are special kind of functions which are not bound to an identifier and are mainly used in functionnal programming as parameters for [higher-order functions a.k.a functor](http://en.wikipedia.org/wiki/Higher-order_function) or as [first-class functions](http://en.wikipedia.org/wiki/First-class_function) in language such as [Haskell](http://en.wikipedia.org/wiki/Haskell_(programming_language)). 

In the Java world, a lambda can be compared with an inner anonymous class. For instance, the following and well known syntax to convert a String to Integer :

new Function<String, Integer>() {
    @Override
    public Integer apply(String input) {
        return Integer.valueOf(input);
    }
};

Could be translated into :

(String x) -> Integer.valueOf(x)

or even more compact

x -> Integer.valueOf(x)


## Why lambdas in Java ? ##


- Short-term goal : make collections able to be processed in parallel and leverage of multi-core architecture (CPU clock frequence does not move, but the number of core on a chip increase). Delegate multhreading plombing form client code to library code, the developer only needs to be focus of the feature.

make it easier to distribute processing of collections over multiple threads
that collections can now organise their own iteration internally, transferring responsibility for parallelisation from client code into library code.
anonymous inner class too verbose

The short-term goals are to support internal iteration of collections, in the interests of efficiently utilising increasingly parallel hardware. The longer-term perspective is to steer Java in a direction that supports a more functional style of programming. Only the short-term goals are being pursued at present, but the designers are being careful to avoid compromising the future of functional programming in Java, which might in the future include fully-fledged function types such as are found in languages such as Haskell and Scala.



- Long-term goal : introduce a functionnal way of programming in Java


## So, lambdas or closures ? ##

In Java 8, lambda = closure. The term lambda is generic.

However, outside of Java, there is a difference between a lambda and a closure.

To make things easier to understand, here an example to show a closure in action :

function foo() {
    var int = 0;

    var bar = function () {
        return int + 1;
    }
    
    return bar;
}

var bar = foo();
bar(); // 1
bar(); // 2


## Syntax of lambdas in Java 8 ##

The basic syntax of a lambda is either :

    (parameters) -> expression
or
    (parameters) -> { statements; }

    Examples :
    1. (int x, int y) -> x + y                          // takes two integers and returns their sum
    2. (x, y) -> x - y                                  // takes two numbers and returns their difference
    3. () -> 42                                         // takes no values and returns 42
    4. (String s) -> System.out.println(s)              // takes a string, prints its value to the console, and returns nothing
    5. x -> 2 * x                                       // takes a number and returns the result of doubling it
    6. c -> { int s = c.size(); c.clear(); return s; }  // takes a collection, clears it, and returns its previous size


## Implementation of lambda in Java 8 ##

- A lambda is **an instance of a Functional Interface**
- A functional interface is an interface with only **one explicitly declared abstract method**, a.k.a **S**ingle **A**bstract **M**ethod (**SAM**). The interface can have default methods (a.k.a virtual extension methods or defender methods)
- Usually flagged with **@FunctionalInterface**
- Well known functional interfaces : **Comparator, Runnable, Callable, ActionListener**
- Where a method with a parameter typed with a Functional Interface is declared, you ca pass a lambda. If a lambda is provided instead of a classic instance of the interface, it will be applied in place of the only method of the given type


A lambda expression itself does not contain the information about which functional interface it is implementing; that information is deduced from the context in which it is used. A lambda can have different target types in different contexts.


## Resources ##

- [Lambdas FAQ](http://www.lambdafaq.org) : great resource, authored like a reference due to Oracle Team which have a look on answers
- [The resources of the resource](http://www.lambdafaq.org/lambda-resources/)
- [Oracle's lambdas tutorial](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)



