package com.intive.rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFamilyRentalRequest {

    @NotNull(message = "rentals is required")
    @Size(min = 3, max = 5, message = "rentals length must between 3 and 5")
    private List<CreateSimpleRentalRequest> rentals;

}
