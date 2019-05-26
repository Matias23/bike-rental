package com.intive.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FamilyRental extends BasicRental{

    private List<SimpleRentalData> rentals;

    @Builder
    public FamilyRental(String rentalId, Integer cost, List<SimpleRentalData> rentals) {
        super(rentalId, cost);
        this.rentals = rentals;
    }

}
