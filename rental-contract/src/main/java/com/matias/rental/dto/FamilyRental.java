package com.matias.rental.dto;

import com.matias.rental.dto.constant.RentalType;
import com.matias.rental.dto.request.CreateRentalRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FamilyRental extends BasicRental{

    private List<CreateRentalRequest> rentals;

    @Builder(builderMethodName = "createFamilyRentalBuilder")
    public FamilyRental(String rentalId, RentalType type, List<CreateRentalRequest> rentals) {
        super(rentalId, type);
        this.rentals = rentals;
    }
}