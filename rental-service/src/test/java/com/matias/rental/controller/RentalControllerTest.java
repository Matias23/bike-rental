package com.matias.rental.controller;

import com.matias.rental.dto.FamilyRental;
import com.matias.rental.dto.Rental;
import com.matias.rental.dto.constant.ErrorMessages;
import com.matias.rental.dto.constant.RentalType;
import com.matias.rental.dto.request.CreateFamilyRentalRequest;
import com.matias.rental.dto.request.CreateRentalRequest;
import com.matias.rental.service.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalControllerTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected RentalRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    private ResponseEntity<Rental> postSingleRental(CreateRentalRequest createRentalRequest) {
        return restTemplate.postForEntity(String.format("http://localhost:%s/v1/rentals/basic", port),
                createRentalRequest, Rental.class);
    }

    private ResponseEntity<FamilyRental> postFamilyRental(CreateFamilyRentalRequest createFamilyRentalRequest) {
        return restTemplate.postForEntity(String.format("http://localhost:%s/v1/rentals/family", port),
                createFamilyRentalRequest, FamilyRental.class);
    }

    @Test
    public void getPort() {
        Assert.assertThat("Should get a random port greater than zero!", port, greaterThan(0));
    }

    @Test
    public void createRental() {
        CreateRentalRequest rentalRequest = buildDummyRentalRequest();
        ResponseEntity<Rental> response = postSingleRental(rentalRequest);

        Assert.assertEquals("Response status doesn't match", HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createRentalNullValuesRequest() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .createRentalRequest()
                .build();
        try {
            postSingleRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount is required and was not present",
                    e.getResponseBodyAsString().contains(ErrorMessages.REQUIRED_AMOUNT));
            Assert.assertTrue("Type is required and was not present",
                    e.getResponseBodyAsString().contains(ErrorMessages.REQUIRED_TYPE));
        }
    }

    @Test
    public void createRentalInvalidValuesRequest() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .createRentalRequest()
                .type(RentalType.FAMILY)
                .amount(-2)
                .build();
        try {
            postSingleRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount was too low",
                    e.getResponseBodyAsString().contains(ErrorMessages.LOW_AMOUNT));
            Assert.assertTrue("Type was invalid",
                    e.getResponseBodyAsString().contains(ErrorMessages.NOT_FAMILY_RENTAL_TYPE));
        }
    }

    @Test
    public void createFamilyRental() {
        CreateFamilyRentalRequest familyRentalRequest = buildDummyFamilyRentalRequest();
        ResponseEntity<FamilyRental> response = postFamilyRental(familyRentalRequest);

        Assert.assertEquals("Response status doesn't match", HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createFamilyRentalNullValuesRequest() {
        CreateFamilyRentalRequest familyRentalRequest = CreateFamilyRentalRequest
                .createFamilyRentalRequestBuilder()
                .build();
        try {
            postFamilyRental(familyRentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Rental is required and was not present",
                    e.getResponseBodyAsString().contains(ErrorMessages.REQUIRED_RENTALS));
            Assert.assertTrue("Type is required and was not present",
                    e.getResponseBodyAsString().contains(ErrorMessages.REQUIRED_TYPE));
        }
    }

    @Test
    public void createFamilyRentalInvalidValuesRequest() {
        CreateFamilyRentalRequest familyRentalRequest = CreateFamilyRentalRequest
                .createFamilyRentalRequestBuilder()
                .type(RentalType.DAILY)
                .rentals(new ArrayList<>())
                .build();
        try {
            postFamilyRental(familyRentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Rentals was too short",
                    e.getResponseBodyAsString().contains(ErrorMessages.RENTALS_SIZE));
            Assert.assertTrue("Type was invalid",
                    e.getResponseBodyAsString().contains(ErrorMessages.FAMILY_RENTAL_TYPE));
        }
    }

    public static CreateRentalRequest buildDummyRentalRequest() {
        return CreateRentalRequest
                .createRentalRequest()
                .type(RentalType.DAILY)
                .amount(2)
                .build();
    }

    public static CreateFamilyRentalRequest buildDummyFamilyRentalRequest() {
        CreateRentalRequest singleRental = buildDummyRentalRequest();
        List<CreateRentalRequest> rentals = new ArrayList<>();
        rentals.add(singleRental);
        rentals.add(singleRental);
        rentals.add(singleRental);
        return CreateFamilyRentalRequest
                .createFamilyRentalRequestBuilder()
                .type(RentalType.FAMILY)
                .rentals(rentals)
                .build();
    }
}
