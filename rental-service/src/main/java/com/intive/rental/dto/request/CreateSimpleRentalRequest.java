package com.intive.rental.dto.request;

import com.intive.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSimpleRentalRequest {

    @NotNull(message = "amount is required")
    @Min(value = 1, message = "amount should be greater than 0")
    private Integer amount;

    @NotNull(message = "rentalType is required")
    private RentalType rentalType;

}
