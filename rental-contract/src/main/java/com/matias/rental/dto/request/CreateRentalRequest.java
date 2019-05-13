package com.matias.rental.dto.request;

import com.matias.rental.dto.constant.ErrorMessages;
import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequest extends BasicCreateRequest{

    @NotNull(message = ErrorMessages.REQUIRED_AMOUNT)
    @Min(value = 0, message = ErrorMessages.LOW_AMOUNT)
    private Integer amount;

    @AssertTrue(message = ErrorMessages.NOT_FAMILY_RENTAL_TYPE)
    public boolean isValid() {
        return type == null || !type.equals(RentalType.FAMILY);
    }

    @Builder(builderMethodName = "createRentalRequest")
    public CreateRentalRequest(RentalType type, Integer amount) {
        super(type);
        this.amount = amount;
    }

}
