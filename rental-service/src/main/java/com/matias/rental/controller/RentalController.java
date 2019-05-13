package com.matias.rental.controller;

import com.matias.rental.dto.FamilyRental;
import com.matias.rental.dto.Rental;
import com.matias.rental.dto.request.CreateFamilyRentalRequest;
import com.matias.rental.dto.request.CreateRentalRequest;
import com.matias.rental.service.RentalService;
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

    @ApiOperation("Stores a single rental")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 409, message = "Rental for given request already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/basic")
    @ResponseStatus(HttpStatus.CREATED)
    public Rental postSingleRental(
            @RequestBody @Validated CreateRentalRequest createRentalRequest) {
        log.info("Received new request for saving bike rental: {}", createRentalRequest);

        return service.postSingleRental(createRentalRequest);
    }

    @ApiOperation("Gets a single rental")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Rental for given id not exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/basic/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public Rental getSingleRental(
            @ApiParam(value = "Rental id", required = true) @PathVariable String rentalId) {
        log.info("Received new request for getting single bike rental with id: {}", rentalId);

        return service.getSingleRental(rentalId);
    }

    @ApiOperation("Stores a family rental")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 409, message = "Rental for given request already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/family")
    @ResponseStatus(HttpStatus.CREATED)
    public FamilyRental postFamilyRental(
            @RequestBody @Validated CreateFamilyRentalRequest createFamilyRentalRequest) {
        log.info("Received new request for saving family bike rental: {}", createFamilyRentalRequest);

        return service.postFamilyRental(createFamilyRentalRequest);
    }

    @ApiOperation("Gets a family rental")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Family rental for given id not exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/family/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public Rental getSingleRental(
            @ApiParam(value = "Rental id", required = true) @PathVariable String rentalId) {
        log.info("Received new request for getting single bike rental with id: {}", rentalId);

        return service.getSingleRental(rentalId);
    }
}
