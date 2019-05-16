package com.intive.rental.dto.request;

import com.intive.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRentalRequest {

    @NotNull(message = "type is required")
    protected RentalType type;

    @Min(value = 1, message = "amount should be greater than 0")
    private Integer amount;

    @Size(min = 3, max = 5, message = "rentals length must between 3 and 5")
    private List<CreateRentalRequest> rentals;

    @AssertTrue(message = "parameters sent do not match with rental type")
    public boolean isValidSingleRental() {
        return type == null || type == RentalType.FAMILY || (amount != null && rentals == null);
    }

    @AssertTrue(message = "parameters sent do not match with family type")
    public boolean isValidFamilyRental() {
        return type == null || !type.equals(RentalType.FAMILY) || (amount == null && rentals != null);
    }

}
