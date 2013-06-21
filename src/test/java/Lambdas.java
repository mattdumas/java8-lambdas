import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.fest.assertions.api.Assertions.assertThat;

public class Lambdas {


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
        List<List<Integer>> lists = newArrayList(Iterables.transform(list, new Function<String, List<Integer>>() {
            @Override
            public List<Integer> apply(String input) {
                return newArrayList(Integer.valueOf(input));
            }
        }));

        List<Integer> ints = newArrayList(Iterables.transform(lists, new Function<List<Integer>, Integer>() {
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


    static <T, U> U reduce(U identity, Iterable<T> iterable, Function2<T, U> function) {
        for (T current : iterable) {
            identity = function.apply(identity, current);
        }

        return identity;
    }


    static interface Function2<T, U> {
        U apply(U a, T b);
    }


}
