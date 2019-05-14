package com.matias.rental.dto.request;

import com.matias.rental.dto.constant.ErrorMessages;
import com.matias.rental.dto.constant.RentalType;
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

    @Min(value = 1, message = ErrorMessages.LOW_AMOUNT)
    private Integer amount;

    @Size(min = 3, max = 5, message = ErrorMessages.RENTALS_SIZE)
    private List<CreateRentalRequest> rentals;

    @AssertTrue(message = ErrorMessages.INVALID_RENTAL_VALUES)
    public boolean isValidSingleRental() {
        return type == null || type == RentalType.FAMILY || (amount != null && rentals == null);
    }

    @AssertTrue(message = ErrorMessages.INVALID_FAMILY_VALUES)
    public boolean isValidFamilyRental() {
        return type == null || !type.equals(RentalType.FAMILY) || (amount == null && rentals != null);
    }

}
