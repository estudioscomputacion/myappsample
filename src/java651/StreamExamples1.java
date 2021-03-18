package java651;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExamples1 {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
//	In Java, the Stream interface is in the java.util.stream package. 
//	There are a few ways to create a finite stream:
        Stream<String> empty = Stream.empty(); 				// count = 0
        Stream<Integer> singleElement = Stream.of(1); 		// count = 1
        Stream<Integer> fromArray = Stream.of(1, 2, 3); 	// count = 2

        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> fromList = list.stream();
        Stream<String> fromListParallel = list.parallelStream();

        //infinite stream, generate random
        Stream<Double> randoms = Stream.generate(Math::random);

        //infinite stream, generate serial numbers: 13579		
        Stream<Integer> oddNumbers = Stream.iterate(1, n -> n + 2);
        System.out.print("oddNumbers: ");
        oddNumbers.limit(5).forEach(System.out::print);

        //Using Common Terminal Operations
        //count()
        Stream<String> str = Stream.of("monkey", "gorilla", "bonobo");
        System.out.println("\n" + str.count()); 			// 3

        //min() max()
        Stream<String> s = Stream.of("monkey", "ape", "bonobo");
        Optional<String> min = s.min((s1, s2) -> s1.length() - s2.length());
        min.ifPresent(System.out::println); 				// ape

//	As an example of where
//	there isn�t a minimum, let�s look at an empty stream:

        Optional<?> minEmpty = Stream.empty().min((s1, s2) -> 0);
        System.out.println(minEmpty.isPresent()); 			// false

//	Since the stream is empty, the comparator is never called and no value is present in the
//	Optional.

//	findAny() and findFirst()		
        Stream<String> st = Stream.of("monkey", "gorilla", "bonobo");
        Stream<String> infinite = Stream.generate(() -> "chimp");
        st.findAny().ifPresent(System.out::println); 		// monkey
        infinite.findAny().ifPresent(System.out::println); 	// chimp		

//	allMatch() , anyMatch() and noneMatch()
//	This example checks whether animal names begin with letters:

        List<String> list1 = Arrays.asList("monkey", "2", "chimp");
        Stream<String> infinite1 = Stream.generate(() -> "chimp");
        Predicate<String> pred = x -> Character.isLetter(x.charAt(0));
        System.out.println(list1.stream().anyMatch(pred)); 	// true
        System.out.println(list1.stream().allMatch(pred));	// false
        System.out.println(list1.stream().noneMatch(pred)); // false
        System.out.println(infinite1.anyMatch(pred)); 		// true		

        //forEach()
        Stream<String> str1 = Stream.of("Monkey", "Gorilla", "Bonobo");
        str1.forEach(System.out::print); 						// MonkeyGorillaBonobo

        System.out.println();

        //reduce()		
        System.out.println("Stream reduce"); 
//	The initial value of an empty String is the identity. The accumulator combines the current
//	result with the current String . 
        Stream<String> stream = Stream.of("w", "o", "l", "f");
        String word = stream.reduce("", (a, b) -> a + b);
        System.out.println(word); 					// wolf

//	Notice how we still have the empty String as the identity. We also still concatenate the
//	String s to get the next value. We can even rewrite this with a method reference:

        Stream<String> stream1 = Stream.of("w", "o", "l", "f");
        String word1 = stream1.reduce("", String::concat);
        System.out.println(word1); 					// wolf

        Stream<String> stream9 = Stream.of("w", "o", "l", "f");
        String word5 = stream9.reduce("[", (a, b) ->  a.toLowerCase()
                                                    + b.toLowerCase() ).concat("]");
        System.out.println(word5); 			// [wolf] 
        
        Stream<String> stream10 = Stream.of("w", "o", "l", "f");
        String word6 = stream10.reduce("[", (a, b) ->  a.toUpperCase()
                                                     + b.toUpperCase() ).concat("]");
        System.out.println(word6); 			// [WOLF]        
        
//	stream q permite aplicar una operacion aritmetica sobre cada
//	elemento de la lista 

        Stream<Integer> stream2 = Stream.of(3, 5, 6);
        System.out.println(stream2.reduce(1, (a, b) -> a * b ));	//90
		
        List<Integer> lista = Arrays.asList(2, 4, 8, 10);
        lista.stream()
                .reduce( (s1, s2) -> s1 * s2 )		
                .ifPresent(System.out::println); 			//640
		
        BinaryOperator<Integer> op = (a, b) -> a * b;
        Stream<Integer> empty1 = Stream.empty();
        Stream<Integer> oneElement = Stream.of(3);
        Stream<Integer> threeElements = Stream.of(3, 5, 6);
        empty1.reduce(op).ifPresent(System.out::print); 		// no output
        oneElement.reduce(op).ifPresent(System.out::println); 		// 3
        threeElements.reduce(op).ifPresent(System.out::println);	// 90		
		
        //collect()
        Stream<String> stream3 = Stream.of("w", "o", "l", "f");
        StringBuilder word2 = stream3.collect(  StringBuilder::new,
                                                StringBuilder::append,  
                                                StringBuilder::append);
        System.out.println( word2 );					//wolf

        Stream<String> stream4 = Stream.of("w", "o", "l", "f");
        TreeSet<String> set = stream4.collect(TreeSet::new, TreeSet::add,
        TreeSet::addAll);
        System.out.println( set );				// [f, l, o, w]

        Stream<String> stream5 = Stream.of("w", "o", "l", "f");
        TreeSet<String> set1 = stream5.collect(Collectors.toCollection(TreeSet::new));
        System.out.println( set1 ); 				// [f, l, o, w]

        //If we didn�t need the set to be sorted, we could make the code even shorter:
        Stream<String> stream6 = Stream.of("w", "o", "l", "f");
        Set<String> set2 = stream6.collect(Collectors.toSet());
        System.out.println( set2 ); 				// [f, w, l, o]

        //filter() 														pag 196
        Stream<String> s1 = Stream.of("monkey", "gorilla", "bonobo");
        s1.filter(x -> x.startsWith("m")).forEach(System.out::println); // monkey

        //distinct()
        Stream<String> s2 = Stream.of("duck", "duck", "duck", "ostrich", "goose", "ostrich");
        s2.distinct().forEach(System.out::println); 	// duckostrichgoose

        //limit() and skip()
        Stream<Integer> s3 = Stream.iterate(1, n -> n + 1);
        s3.skip(5).limit(2).forEach(System.out::println); 		// 6 7

        Stream<Integer> s4 = Stream.of(9, 15, 18, 27, 32);
        s4.skip(3).limit(2).forEach(System.out::println); 		// 27 32

        //map()
        /*The map() method on streams is for transforming data. Don�t confuse it
        with the Map interface, which maps keys to values.
        this code converts a list of String objects to a list of Integer s representing
        their lengths:*/

        Stream<String> s5 = Stream.of("monkey", "gorilla", "bonobo");
        s5.map(String::length).forEach(System.out::print); 		// 676

         /* Remember that String::length is shorthand for the lambda x -> x.length() ,
         * which clearly shows it is a function that turns a String into an Integer .*/

//		flatMap()
        List<String> zero = Arrays.asList();
        List<String> one = Arrays.asList("Bonobo");
        List<String> two = Arrays.asList("Mama Gorilla", "Baby Gorilla");
        Stream<List<String>> animals = Stream.of(zero, one, two);
        animals.flatMap(l -> l.stream()).forEach(System.out::println);
                                                                                                                                        //	Bonobo
                                                                                                                                        //	Mama Gorilla
                                                                                                                                        //	Baby Gorilla
//	flatMap() removed the empty list completely and changed all elements of each
//	list to be at the top level of the stream.

        //sorted()		
//      The sorted() method returns a stream with the elements sorted. Just like
//	sorting arrays, Java uses natural ordering unless we specify a comparator.

        Stream<String> s6 = Stream.of("brown-", "bear-");
        s6.sorted().forEach(System.out::println); 	// bear- brown-

        Stream<String> s7 = Stream.of("brown bear-", "grizzly-");
        s7.sorted( Comparator.reverseOrder() )
          .forEach(System.out::println); 	// grizzly- brown bear-

        //peek()
        Stream<String> stream7 = Stream.of("black bear", "brown bear", "grizzly");
        long count = stream7.filter(st2 -> st2.startsWith("g"))
        .peek(System.out::println).count(); 		// grizzly
        System.out.println(count); 			// 1

//	Remember that peek() is intended to perform an operation without changing the result.
//	Here�s a straightforward stream pipeline that doesn�t use peek().
        System.out.println("Tamaño de las listas:");
        List<Integer>   numbers = new ArrayList<>();
        List<Character> letters = new ArrayList<>();
        numbers.add(1);
        letters.add('a');
        Stream<List<?>> stream8 = Stream.of(numbers, letters);
        stream8.map(List::size).forEach(System.out::println); 	// 11

//	We can add a proper peek() operation:
        StringBuilder builder = new StringBuilder();
        Stream<List<?>> good = Stream.of(numbers, letters);
        good.peek(l -> builder.append(l)).map(List::size).forEach(System.out::println); // 11
        System.out.println(builder);			  // [1][a]

        System.out.println("\n itero 1, n -> n + 1   ; limit 5:");                
        Stream<Integer> s8 = Stream.iterate(1, n -> n + 1);
        s8.limit(5).forEach(System.out::println);                 

        System.out.println("\n itero 0, n -> n + 1   ; limit 5:");                
        Stream<Integer> s9 = Stream.iterate(0, n -> n + 1);
        s9.limit(5).forEach(System.out::println);

        System.out.println("\n map( x -> x+1)  sobre lista 1,2,3:");                                
        List<Integer>   numbers2 = new ArrayList<>();
        numbers2.add(1);
        numbers2.add(2);
        numbers2.add(3);
        numbers2.stream().map( x -> x+1).forEach(System.out::println);
	}

}
