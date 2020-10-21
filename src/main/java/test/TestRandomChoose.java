package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-21 10:07
 **/
public class TestRandomChoose {
    public static final Random RANDOM = new Random(10);

    public static void main(String[] args) {
        Item a = new Item("a", .2);
        Item b = new Item("b", .3);
        Item c = new Item("c", .4);
        Item d = new Item("d", .1);
        List<Item> items = Arrays.asList(a, b, c, d);
        List<Item> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Item item = randomChooseOne(items);
            result.add(item);
        }
        double sizeA = (int) result.stream().filter(e -> e.getName().equals("a")).count() / 100.;
        double sizeB = (int) result.stream().filter(e -> e.getName().equals("b")).count() / 100.;
        double sizeC = (int) result.stream().filter(e -> e.getName().equals("c")).count() / 100.;
        double sizeD = (int) result.stream().filter(e -> e.getName().equals("d")).count() / 100.;
        System.out.println(Arrays.asList(sizeA, sizeB, sizeC, sizeD));
    }

    public static Item randomChooseOne(List<Item> select) {
        double p = RANDOM.nextDouble();
//        System.out.println("p=" + p);
        double cumulativeProbability = 0.0;
        Item result = null;
        for (Item item : select) {
            cumulativeProbability += item.getProbability();
            if (p <= cumulativeProbability) {
                result = item;
                break;
            }
        }
        return result;
    }

    static class Item {
        private final String name;
        private final double probability;

        public Item(String name, double probability) {
            this.name = name;
            this.probability = probability;
        }

        public String getName() {
            return name;
        }

        public double getProbability() {
            return probability;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", probability=" + probability +
                    '}';
        }
    }
}
