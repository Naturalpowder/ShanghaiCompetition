package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TimeRegion {

    public static void main(String[] args) {
        TimeRegion timeRegion = new TimeRegion("周一至周日 08:00-20:00  2020-05-03至2020-05-05 10:00-19:30\n");
        System.out.println(timeRegion.intersect(12, 15));
    }

    private final String shop_hours;
    private final List<TimeSection> sections;

    public TimeRegion(String shop_hours) {
        this.shop_hours = shop_hours;
//        System.out.println(shop_hours);
        sections = parse();
//        System.out.println(sections);
//        System.out.println("---------------");
    }

    private List<TimeSection> parse() {
        if (shop_hours.equals("全天"))
            return Collections.singletonList(new TimeSection(0, 24));
        if (shop_hours.isEmpty())
            return new ArrayList<>();
        List<TimeSection> sections = new ArrayList<>();
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(shop_hours);
        String num = m.replaceAll("").trim();
        if (num.length() % 8 == 7)
            num = "0".concat(num);
        if (num.length() % 8 == 0) {
            for (int i = 0; i < num.length(); i += 8) {
                String a = num.substring(i, i + 4);
                String b = num.substring(i + 4, i + 8);
                double start = parseTime(a);
                double end = parseTime(b);
                sections.add(new TimeSection(start, end));
                double length = sections.stream().collect(Collectors.summarizingDouble(TimeSection::getLength)).getSum();
                if (length >= 8)
                    break;
            }
            return sections;
        } else return new ArrayList<>();
    }

    public boolean intersect(double start, double end) {
        boolean intersect = false;
        for (TimeSection section : sections) {
            intersect |= section.intersect(start, end);
        }
        return intersect;
    }

    private double parseTime(String x) {
        double a = Double.parseDouble(x.substring(0, 2));
        double b = Double.parseDouble(x.substring(2, 4));
        return a + b / 60;
    }

    public List<TimeSection> getSections() {
        return sections;
    }

    static class TimeSection {
        private final double start, end;

        public TimeSection(double start, double end) {
            this.start = start;
            this.end = end > start ? end : end + 24;
        }

        public double getLength() {
            return Math.abs(end - start);
        }

        public boolean intersect(double start, double end) {
            if (this.start >= end) return false;
            return !(this.end <= start);
        }

        @Override
        public String toString() {
            return "TimeSection{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
