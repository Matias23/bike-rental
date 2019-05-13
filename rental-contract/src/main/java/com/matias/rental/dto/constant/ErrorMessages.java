package com.matias.rental.dto.constant;

public class ErrorMessages {

    public static final String REQUIRED_TYPE = "type is required";

    public static final String REQUIRED_AMOUNT = "amount is required";
    public static final String LOW_AMOUNT = "amount should be greater than 0";
    public static final String NOT_FAMILY_RENTAL_TYPE = "rentalType should not be FAMILY";

    public static final String REQUIRED_RENTALS = "rentals is required";
    public static final String RENTALS_SIZE = "rentals length must between 3 and 5";
    public static final String FAMILY_RENTAL_TYPE = "rentalType should not be FAMILY";
}
