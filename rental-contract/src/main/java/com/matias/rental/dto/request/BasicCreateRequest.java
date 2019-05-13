package com.matias.rental.dto.request;

import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicCreateRequest {

    @NotNull(message = "type is required")
    protected RentalType type;
}
