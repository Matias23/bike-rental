package com.matias.rental.dto.request;

import com.matias.rental.dto.constant.ErrorMessages;
import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateFamilyRentalRequest extends BasicCreateRequest{

    @NotNull(message = ErrorMessages.REQUIRED_RENTALS)
    @Size(min = 3, max = 5, message = ErrorMessages.RENTALS_SIZE)
    private List<CreateRentalRequest> rentals;

    @AssertTrue(message = ErrorMessages.FAMILY_RENTAL_TYPE)
    public boolean isValidFamilyType() {
        return type == null || type.equals(RentalType.FAMILY);
    }

    @Builder(builderMethodName = "createFamilyRentalRequestBuilder")
    public CreateFamilyRentalRequest(RentalType type, List<CreateRentalRequest> rentals) {
        super(type);
        this.rentals = rentals;
    }

}
