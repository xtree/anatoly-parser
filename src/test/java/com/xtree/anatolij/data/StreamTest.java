package com.xtree.anatolij.data;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void blbost() throws Exception {
        Stream.of("a.b.c:d".split("\\.")).flatMap(new Function<String, Stream<String>>() {
            @Override
            public Stream<String> apply(String s) {
                //System.out.println("f:"+s);
                return Stream.of(s,null);
            }
        }).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s != null;
            }
        }).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

    }

    @Test
    public void blaa() throws Exception {
        Stream.of("a.b.c:d".split("\\.")).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

    }
}
