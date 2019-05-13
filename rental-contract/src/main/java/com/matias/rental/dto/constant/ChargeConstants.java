package com.matias.rental.dto.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChargeConstants {

    public static final Double FAMILY_DISCOUNT = 0.3;

    public static final Map<RentalType, Integer> CHARGE_CONSTANTS_MAP;
    static {
        Map<RentalType, Integer> staticMap = new HashMap<>();
        staticMap.put(RentalType.HOURLY, 5);
        staticMap.put(RentalType.DAILY, 20);
        staticMap.put(RentalType.WEEKLY, 60);
        CHARGE_CONSTANTS_MAP = Collections.unmodifiableMap(staticMap);
    }
}
