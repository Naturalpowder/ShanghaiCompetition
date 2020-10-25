package util;

import java.util.Random;

public class Container {
    /**
     * Center of the calculated region
     */
    public final static double LAT = (31.230918 + 31.248808) / 2, LNG = (121.492236 + 121.518305) / 2;
    public static Random RANDOM;
    public final static int MORNING = 0, NOON = 1, EVENING = 2;
    public static int TIME = 0;
    public final static double SECTION = 2666., MIN = 1;//Total: 8821. ,Morning_Noon_Evening: 6969. , Each_Building_Morning_Noon_Evening:2666. , Each_Building_FullDay: 3508.

    public static String getName() {
        switch (Container.TIME) {
            case Container.MORNING:
                return "Morning";
            case Container.NOON:
                return "Noon";
            case Container.EVENING:
                return "Evening";
            default:
                return "FullDay";
        }
    }

}
