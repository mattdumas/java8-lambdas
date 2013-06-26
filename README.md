Java 8 lambdas tutorial
=======================

The goal of this tutorial is to introduce the lambdas in Java 8, also known as [JSR 335 - Lambda Expressions for the JavaTM Programming Language](http://jcp.org/en/jsr/detail?id=335).

The tutorial is based on the version ["Feature Complete"](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-June/002686.html) of Java 8 (b94) and can be downloaded [here](https://jdk8.java.net/download.html).


## How the tutorial works ? ##

The tutorial consists in a testing suite **org.mdumas.lambdas.Lambdas**. Each test need to be passed to complete the tutorial. 

Several tests are a comparison between the current syntax of Java 1.6 / 1.7 which emulates a kind of functional programming with the help of the [Guava](https://code.google.com/p/guava-libraries) library and the new one in Java 8.



## What's a lambda expression ##

According to Wikipedia, a [lambda expression][1] is an anonymous function. Based on the work of [Alonzo Church](http://en.wikipedia.org/wiki/Alonzo_Church) on his [lambda calculus](http://en.wikipedia.org/wiki/Lambda_calculus) and used  for the first time in a programming language with [Lisp][2], lambda expressions are special kind of functions which are not bound to an identifier and are mainly used in functionnal programming as parameters for [higher-order functions a.k.a functor](http://en.wikipedia.org/wiki/Higher-order_function) or as [first-class functions](http://en.wikipedia.org/wiki/First-class_function) in language such as [Haskell](http://en.wikipedia.org/wiki/Haskell_(programming_language)). 

In the Java world, a lambda can be compared with an inner anonymous class. For instance, the following and well known syntax to convert a String to Integer :

[1]: http://en.wikipedia.org/wiki/Lambda_(programming)
[2]: http://en.wikipedia.org/wiki/Lisp_(programming_language)

```
new Function<String, Integer>() {
    @Override
    public Integer apply(String input) {
        return Integer.valueOf(input);
    }
};
```

Could be translated into :

```
(String x) -> Integer.valueOf(x)
```

or even more compact

```
x -> Integer.valueOf(x)
```


## Why lambdas in Java ? ##


- Short-term goal : enable collections to be processed in parallel and leverage of multi-cores architecture (CPU clock frequence does not move, but the number of core on a chip increase). Delegate multhreading plumbing form client code to library code, the developer only needs to be focused on the feature.
- Long-term goal : introduce a functionnal way of programming in Java

Notes : make it easier to distribute processing of collections over multiple threads
that collections can now organise their own iteration internally, transferring responsibility for parallelisation from client code into library code.
anonymous inner class too verbose. The short-term goals are to support internal iteration of collections, in the interests of efficiently utilising increasingly parallel hardware. The longer-term perspective is to steer Java in a direction that supports a more functional style of programming. Only the short-term goals are being pursued at present, but the designers are being careful to avoid compromising the future of functional programming in Java, which might in the future include fully-fledged function types such as are found in languages such as Haskell and Scala.


## So, lambdas or closures ? ##

In Java 8, lambda = closure. Lambda expressions will be implemented by means of closures, so the two terms have come to be used interchangeably in the community.

However, outside of Java, there is a difference between a lambda and a closure.

To make things easier to understand, here an example to show a closure in action :

```
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
```


## Syntax of lambdas in Java 8 ##

The basic syntax of a lambda is either :
```
    (parameters) -> expression
```    
or
```
    (parameters) -> { statements; }
```    

    Examples :
    1. (int x, int y) -> x + y                          // takes two integers and returns their sum
    2. (x, y) -> x - y                                  // takes two numbers and returns their difference
    3. () -> 42                                         // takes no values and returns 42
    4. (String s) -> System.out.println(s)              // takes a string, prints its value to the console, and returns nothing
    5. x -> 2 * x                                       // takes a number and returns the result of doubling it
    6. c -> { int s = c.size(); c.clear(); return s; }  // takes a collection, clears it, and returns its previous size


## Implementation of lambda in Java 8 ##

- A lambda is **an instance of a Functional Interface**. May be thought of as an anonymous representation of a function descriptor of a functional interface
- A functional interface is an interface with only **one explicitly declared abstract method**, a.k.a **S**ingle **A**bstract **M**ethod (**SAM**). The interface can have default methods (a.k.a virtual extension methods or defender methods) and/or abstract method inherited from Object explicitly
- Usually flagged with **@FunctionalInterface**
- Well known functional interfaces : **Comparator, Runnable, Callable, ActionListener**
- Where a method with a parameter typed with a Functional Interface is declared, you ca pass a lambda. If a lambda is provided instead of a classic instance of the interface, it will be applied in place of the only method of the given type
- The expected type for a lamba (assignement, parameter, etc...) is called the **target type** and need to be a functional interface. Depdending of the context, a same lambda can be compatible with several functional interfaces :
    - `IntOperation io = x -> x * 2;` 
    - `DoubleOperation do = x -> x * 2;` 
- **Variable Capture** make lambdas able to read and write instance and static variables in the lambda body. More restricted for local variables, variable capture is not allowed unless they are effectively final, a concept introduced in Java 8. Effectively final = its initial value is never changed (including within the body of a lambda expression). A less verbose way of defining final variables.
- **Method reference** is an alternative way for representing function descriptor and can rempalce lambda expression :
    - **static** : `Integer::valueOf` -> `list.stream().map(Integer::valueOf)`
    - **reference** : `System.out::println` -> `list.forEach(System.out::println)`
    - **constructor** : `Integer::new` -> `list.stream(Integer::new)`
- **Default methods** (also called virtual extension methods or defender methods) is a new feature in Java 8 to enable interfaces to evolve without introducing incompatibility with existing implementations by providing default method implementations.


## What about the Java Collections Framework ? ##
- 15 years old, compatibility problems resolved with default methods
- The center of Java 8 changes. Leverage of multi-cores architecture, applications have to be able to divide their workload among many different threads, with each thread being capable of running independently on its “own” core
- Current usages of collections are bulk operations : apply transformations (filtering, mapping), often finally summarising it in a single operation such as summing numeric elements.  Requires the creation of temporary collections to hold the intermediate results of these transformations 
- Can be replaced by a “Pipes and Filters” pattern : elimination of intermediate variables, reduction of intermediate storage, lazy evaluation, and more flexible and composable operation definitions.
- Moreover, if each operation in the pipeline is defined appropriately, the pipeline as a whole can often be automatically parallelised (split up for parallel execution on multicore processors). The role of pipes, the connectors between pipeline operations, is taken in the Java 8 libraries by implementations of the **Stream interface** 

- A **stream** is a sequence of values - package **java.util.stream **.
- To obtain a stream : 
    - form a collections : **collection.stream()**
    - static methods in Stream interface
- A stream is either partially evaluated—some of its elements remain to be generated—or exhausted, when its elements are all used up.
- A stream can have as its source an array, a collection, a generator function, or an IO channel, the result of an operation on another stream (method chaining)
- Calls on intermediate operations are often chained together in the style of a fluent API, forming a pipeline
- Stream types define intermediate operations (resulting in new streams), e.g. map, and terminal operations (resulting in non-stream values), e.g. forEach
- Terminal operations terminate a method chain. Terminal operations are also called eager because invoking them causes them to consume values from the pipeline immediately, whereas intermediate operations, also called lazy, only produce values on demand

## Why do not add Stream operations directly on Collection / Iterable ? ##
- Early drafts of the API exposed methods like filter, map, and reduce on Collection or Iterable
- Bad UX with this design led to a more formal separation of the “stream” methods into their own abstraction. Reasons included :
    - ambiguity : `strings.removeAll(s -> s.length() == 0);` and `strings.filter(s -> s.length() == 0);`
    - With lazy methods added to Collection, users were confused by a perceived—but erroneous—need to reason about whether the collection was in “lazy mode” or “eager mode”
    - The more methods added to Collection, the greater the chance of name collisions with existing third-party implementations. By only adding a few methods (stream, parallel) the chance for conflict is greatly reduced
    - A view transformation is still needed to access a parallel view; the asymmetry between the sequential and the parallel stream views was unnatural. For example :
        - `coll.filter(...).map(...).reduce(...)` and `coll.parallel().filter(...).map(...).reduce(...);`. This asymmetry would be particularly obvious in the API documentation, where Collection would have many new methods to produce sequential streams, but only one to produce parallel streams, which would then have all the same methods as Collection. Factoring these into a separate interface, StreamOps say, would not help; that would still, counterintuitively, need to be implemented by both Stream and Collection;
    - A uniform treatment of views also leaves room for other additional views in the future


## Resources ##

- [Lambdas FAQ](http://www.lambdafaq.org) : great resource, an authoritative popular reference due to Oracle Team which have a look on answers
- [The resources of the resource](http://www.lambdafaq.org/lambda-resources/)
- [Oracle's lambdas tutorial](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)



