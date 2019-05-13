package com.matias.rental.service.kafka;

import com.matias.rental.dto.Rental;
import com.matias.rental.dto.constant.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FamilyRentalMessage {

    private String rentalId;

    private RentalType type;

    private Integer amount;

    private List<Rental> rentals;
}
