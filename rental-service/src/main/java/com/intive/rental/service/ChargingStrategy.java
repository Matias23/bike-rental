package com.intive.rental.service;

import com.intive.rental.dto.FamilyRental;
import com.intive.rental.dto.SimpleRental;
import com.intive.rental.dto.SimpleRentalData;
import com.intive.rental.dto.constant.RentalType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChargingStrategy {

    @Value("${charge.family}")
    private Double FAMILY_DISCOUNT;

    @Value("${charge.hourly}")
    private Integer HOURLY_CHARGE;

    @Value("${charge.daily}")
    private Integer DAILY_CHARGE;

    @Value("${charge.weekly}")
    private Integer WEEKLY_CHARGE;

    public Integer getCost(SimpleRental rental) {
        return getCostForType(rental.getAmount(), rental.getRentalType());
    }

    public Integer getCost(FamilyRental family) {
        int amount = 0;
        for (SimpleRentalData rental : family.getRentals()) {
            amount += getCostForType(rental.getAmount(), rental.getRentalType());
        }
        return new Double((1 - FAMILY_DISCOUNT) * amount).intValue();
    }

    private Integer getCostForType(int amount, RentalType type) {
        switch (type) {
            case HOURLY: return amount * HOURLY_CHARGE;
            case DAILY: return amount * DAILY_CHARGE;
            case WEEKLY: return amount * WEEKLY_CHARGE;
            default: return amount;
        }
    }
}
