package com.intive.rental.dto;

import com.intive.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimpleRental extends BasicRental{

    private Integer amount;

    private RentalType rentalType;

    @Builder
    public SimpleRental(String rentalId, Integer cost, Integer amount, RentalType rentalType) {
        super(rentalId, cost);
        this.amount = amount;
        this.rentalType = rentalType;
    }

}
