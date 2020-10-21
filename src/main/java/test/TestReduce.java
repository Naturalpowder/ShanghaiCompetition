package test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 20:02
 **/
public class TestReduce {
    public static void main(String[] args) {
        Number a = new Number(2);
        Number b = new Number(3);
        Number c = new Number(5);
        List<Number> numbers = Arrays.asList(a, b, c);
        System.out.println(numbers.stream().reduce((o1, o2) -> new Number(o1.a + o2.a)).get());
    }
}

class Number {
    int a;

    public Number(int a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "Number{" +
                "a=" + a +
                '}';
    }
}
