package com.intive.rental.controller;

import com.intive.rental.dto.Rental;
import com.intive.rental.dto.request.CreateRentalRequest;
import com.intive.rental.service.RentalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "v1/rentals")
@RestController
@RequestMapping("v1/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService service;

    @ApiOperation("Stores a rental")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 409, message = "Rental for given request already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rental postRental(
            @RequestBody @Validated CreateRentalRequest createRequest) {
        log.info("Received new request for saving bike rental: {}", createRequest);

        return service.postRental(createRequest);
    }

    @ApiOperation("Gets a rental")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Rental for given id not exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public Rental getRental(
            @ApiParam(value = "Rental id", required = true) @PathVariable String rentalId) {
        log.info("Received new request for getting bike rental with id: {}", rentalId);

        return service.getRental(rentalId);
    }

}
