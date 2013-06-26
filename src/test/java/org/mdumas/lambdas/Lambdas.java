package org.mdumas.lambdas;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.fest.assertions.api.Assertions.assertThat;

public class Lambdas {


//    Syntax :
//    The basic syntax of a lambda is either
//
//            (parameters) -> expression
//    or
//            (parameters) -> { statements; }
//
//    Examples :
//    1. (int x, int y) -> x + y                          // takes two integers and returns their sum
//    2. (x, y) -> x - y                                  // takes two numbers and returns their difference
//    3. () -> 42                                         // takes no values and returns 42
//    4. (String s) -> System.out.println(s)              // takes a string, prints its value to the console, and returns nothing
//    5. x -> 2 * x                                       // takes a number and returns the result of doubling it
//    6. c -> { int s = c.size(); c.clear(); return s; }  // takes a collection, clears it, and returns its previous size


    List<String> list = newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");


    @Test
    public void should_filter_even_number() {
        // Given
        List<String> filterGuava = newArrayList(filter(list, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return Integer.valueOf(input) % 2 == 0;
            }
        }));

        // When
        List<String> filterJava8 = list.stream().filter(x -> Integer.valueOf(x) % 2 == 0).collect(Collectors.toList());

        // Then
        assertThat(filterGuava).isEqualTo(filterJava8);
    }

   @Test
   public void should_map_to_int() {
       // Given
       List<Integer> mapGuava = transform(list, new Function<String, Integer>() {
           @Override
           public Integer apply(String input) {
               return Integer.valueOf(input);
           }
       });

       // When
       List<Integer> mapJava8 = list.stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());

       // Then
       assertThat(mapGuava).isEqualTo(mapJava8);
   }

    @Test
    public void should_map_to_int_with_constructor_reference_method() {
        // Given
        List<Integer> mapGuava = transform(list, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        });

        // When
        List<Integer> mapJava8 = list.stream().map(Integer::new).collect(Collectors.toList());

        // Then
        assertThat(mapGuava).isEqualTo(mapJava8);
    }

    @Test
    public void should_map_to_int_with_static_reference_method() {
        // Given
        List<Integer> mapGuava = transform(list, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        });

        // When
        List<Integer> mapJava8 = list.stream().map(Integer::valueOf).collect(Collectors.toList());

        // Then
        assertThat(mapGuava).isEqualTo(mapJava8);
    }

    @Test
    public void should_reduce_by_concatenating_all_strings() {
        // Given
        String reduceGuava = reduce("", list, new Function2<String, String>() {
            @Override
            public String apply(String a, String b) {
                return a + b;
            }
        });

        // When
        String reduceJava8 = list.stream().reduce("", (x, y) -> x + y);

        // Then
        assertThat(reduceGuava).isEqualTo(reduceJava8);
    }

    @Test
    public void should_reduce_by_summing_all_numbers() {
        // Given
        int reduceGuava = reduce(0, list, new Function2<String, Integer>() {
            @Override
            public Integer apply(Integer a, String b) {
                return a + Integer.valueOf(b);
            }
        });

        // When
        int reduceJava8 = list.stream().reduce(0, (x, y) -> x + Integer.valueOf(y), (x, y) -> x + y);

        // Then 
        assertThat(reduceGuava).isEqualTo(reduceJava8);
    }


    @Test
    public void should_flat_map() {
        // Given
        List<List<Integer>> lists = newArrayList(transform(list, new Function<String, List<Integer>>() {
            @Override
            public List<Integer> apply(String input) {
                return newArrayList(Integer.valueOf(input));
            }
        }));

        List<Integer> ints = newArrayList(transform(lists, new Function<List<Integer>, Integer>() {
            @Override
            public Integer apply(List<Integer> input) {
                return input.get(0);
            }
        }));

        // When
        List<Integer> intsJava8 = list.stream().flatMap(x -> newArrayList(x).stream().map(y -> Integer.valueOf(y))).collect(Collectors.toList());

        // Then
       assertThat(ints).isEqualTo(intsJava8);
    }

    @Test
    public void should_remove_element_if_not_even() {
        // Given

        // When
        boolean result = list.removeIf(x -> Integer.valueOf(x) % 2 != 0);

        // Then
        assertThat(result).isTrue();
        assertThat(list).hasSize(5);
    }

    @Test
    public void should_use_for_each() {
        list.forEach(System.out::println);
    }

    @Test
    public void should_sort_desc() {
        // Given

        // When
        list.sort((x, y) -> Integer.valueOf(y) - Integer.valueOf(x));

        // Then
        assertThat(list).isEqualTo(newArrayList("10", "9", "8", "7", "6", "5", "4", "3", "2", "1"));
    }

    @Test
    public void should_convert_to_int_and_sort_desc_with_static_method_reference() {
        // Given
        List<String> list = newArrayList("10", "9", "8", "7", "6", "5", "4", "3", "2", "1");

        // When
        List<Integer> result = list.stream().map(x -> Integer.valueOf(x)).sorted(Integer::compare).collect(Collectors.toList());

        // Then
        assertThat(result).isEqualTo(newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void should_filter_even_numbers_in_parallel() {
        list.parallelStream().filter(x -> {
           System.out.println("Thread : " + Thread.currentThread().getName());
           return Integer.valueOf(x) % 2 == 0;
        }).collect(Collectors.toList());
    }

    @Test
    public void should_filter_even_numbers_map_to_int_and_add_2_sort_in_parallel_and_get_first_if_exists() {
        // Given

        // When
        Optional<Integer> first = list.stream().filter(x -> Integer.valueOf(x) % 2 == 0)
                                               .map(x -> Integer.valueOf(x) + 2)
                                               .parallel()
                                               .sorted((x, y) -> y - x)
                                               .findFirst();

        // Then
        assertThat(first.isPresent()).isTrue();
        assertThat(first.get()).isEqualTo(12);
    }


    // Assignement before use rule for local variables
    UnaryOperator<Integer> factorial = i -> i == 0 ? 1 : i * factorial.apply(i - 1);

    @Test
    public void should_compute_factorial_in_a_recursive_way() {
        // Given

        // When
        Integer result = factorial.apply(10);

        // Then
        assertThat(result).isEqualTo(3628800);
    }




    static <T, U> U reduce(U identity, Iterable<T> iterable, Function2<T, U> function) {
        for (T current : iterable) {
            identity = function.apply(identity, current);
        }

        return identity;
    }


    @FunctionalInterface
    static interface Function2<T, U> {
        U apply(U a, T b);
    }


}
