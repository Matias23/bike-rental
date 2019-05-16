package com.intive.rental;

import com.intive.rental.dto.Rental;
import com.intive.rental.dto.constant.RentalType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentalApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void hourlyRentalTest() {
        Rental hourly = Rental.builder().type(RentalType.HOURLY).amount(3).build();
        Assert.assertEquals("Hourly charge is not right", Double.valueOf(15), hourly.getCharge());
    }

    @Test
    public void dailyRentalTest() {
        Rental daily = Rental.builder().type(RentalType.DAILY).amount(3).build();
        Assert.assertEquals("Daily charge is not right", Double.valueOf(60), daily.getCharge());
    }

    @Test
    public void weeklyRentalTest() {
        Rental weekly = Rental.builder().type(RentalType.WEEKLY).amount(2).build();
        Assert.assertEquals("Weekly charge is not right", Double.valueOf(120), weekly.getCharge());
    }

    @Test
    public void familyRentalTest() {
        Rental hourly = Rental.builder().type(RentalType.HOURLY).amount(4).build();
        Rental daily = Rental.builder().type(RentalType.DAILY).amount(3).build();
        Rental weekly = Rental.builder().type(RentalType.WEEKLY).amount(2).build();
        List<Rental> rentals = new ArrayList<>();
        rentals.add(hourly);
        rentals.add(daily);
        rentals.add(weekly);
        Rental family = Rental.builder().type(RentalType.FAMILY).rentals(rentals).build();
        Assert.assertEquals("Family charge is not right", Double.valueOf(140), family.getCharge());
    }

}
