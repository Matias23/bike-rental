package com.matias.rental.dto;

import com.matias.rental.dto.constant.ChargeConstants;
import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    private String rentalId;

    private RentalType type;

    private Integer amount;

    private List<Rental> rentals;

    public Double getCharge(){
        if (RentalType.FAMILY != type) {
            return (double) amount * ChargeConstants.CHARGE_CONSTANTS_MAP.get(type);
        }
        double sum = 0;
        for (Rental r : rentals) {
            sum += r.getCharge();
        }
        return (1 - ChargeConstants.FAMILY_DISCOUNT) * sum;

    }
}
