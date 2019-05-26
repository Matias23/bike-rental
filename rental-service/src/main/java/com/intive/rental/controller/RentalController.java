package com.intive.rental.controller;

import com.intive.rental.dto.FamilyRental;
import com.intive.rental.dto.SimpleRental;
import com.intive.rental.dto.request.CreateFamilyRentalRequest;
import com.intive.rental.dto.request.CreateSimpleRentalRequest;
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

    @ApiOperation("Stores a simple rental")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 409, message = "Single rental for given request already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/simple")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleRental postSingleRental(
            @RequestBody @Validated CreateSimpleRentalRequest simpleRentalRequest) {
        log.info("Received new request for saving simple bike rental: {}", simpleRentalRequest);

        return service.postSimpleRental(simpleRentalRequest);
    }

    @ApiOperation("Stores a family rental")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 409, message = "Family rental for given request already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/family")
    @ResponseStatus(HttpStatus.CREATED)
    public FamilyRental postFamilyRental(
            @RequestBody @Validated CreateFamilyRentalRequest familyRentalRequest) {
        log.info("Received new request for saving family bike rental: {}", familyRentalRequest);

        return service.postFamilyRental(familyRentalRequest);
    }

    @ApiOperation("Gets a simple rental")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Simple rental for given id not exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/simple/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleRental getRental(
            @ApiParam(value = "rental id", required = true) @PathVariable String rentalId) {
        log.info("Received new request for getting bike rental with id: {}", rentalId);

        return service.getRental(rentalId);
    }

    @ApiOperation("Gets a family rental")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Family rental for given id not exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/family/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public FamilyRental getFamilyRental(
            @ApiParam(value = "rental id", required = true) @PathVariable String rentalId) {
        log.info("Received new request for getting family bike rental with id: {}", rentalId);

        return service.getFamilyRental(rentalId);
    }

}
