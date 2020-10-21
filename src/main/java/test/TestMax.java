package test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 10:45
 **/
public class TestMax {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 5, 7, 2, 4);
        Integer i = Collections.min(list, (o1, o2) -> {
            return o1 - o2;
        });
        System.out.println(i);
    }
}
