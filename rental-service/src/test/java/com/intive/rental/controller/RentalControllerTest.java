package com.intive.rental.controller;

import com.intive.rental.dto.request.CreateRentalRequest;
import com.intive.rental.dto.Rental;
import com.intive.rental.dto.constant.RentalType;
import com.intive.rental.service.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @MockBean
    private KafkaProducer<String, Rental> rentalMessageProducer;

    @Before
    public void setUp() {
        repository.deleteAll();
        doReturn(null).when(rentalMessageProducer).send(any());
    }

    private ResponseEntity<Rental> postRental(CreateRentalRequest createRentalRequest) {
        return restTemplate.postForEntity(String.format("http://localhost:%s/v1/rentals", port),
                createRentalRequest, Rental.class);
    }

    private ResponseEntity<Rental> getRental(String rentalId) {
        return restTemplate.getForEntity(String.format("http://localhost:%s/v1/rentals/{rentalId}", port),
                Rental.class, rentalId);
    }

    @Test
    public void getPort() {
        Assert.assertThat("Should get a random port greater than zero!", port, greaterThan(0));
    }

    @Test
    public void createRental() {
        CreateRentalRequest rentalRequest = buildDummyRentalRequest();
        ResponseEntity<Rental> response = postRental(rentalRequest);

        Assert.assertEquals("Response status doesn't match", HttpStatus.CREATED, response.getStatusCode());
        verify(rentalMessageProducer, times(1)).send(any());
    }

    @Test
    public void createRentalNullValuesRequest() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Type is required and was not present",
                    e.getResponseBodyAsString().contains("type is required"));
        }
    }

    @Test
    public void createRentalLowAmountRequest() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.DAILY)
                .amount(-2)
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount was too low",
                    e.getResponseBodyAsString().contains("amount should be greater than 0"));
        }
    }

    @Test
    public void createRentalNoAmountRequest() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.DAILY)
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount was not present",
                    e.getResponseBodyAsString().contains("parameters sent do not match with rental type"));
        }
    }

    @Test
    public void createSingleRentalWithRentalsIncluded() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.DAILY)
                .amount(3)
                .rentals(buildRentalRequestList())
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Rental list should not be present",
                    e.getResponseBodyAsString().contains("parameters sent do not match with rental type"));
        }
    }

    @Test
    public void createFamilyRental() {
        CreateRentalRequest familyRentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.FAMILY)
                .rentals(buildRentalRequestList())
                .build();
        ResponseEntity<Rental> response = postRental(familyRentalRequest);

        Assert.assertEquals("Response status doesn't match", HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createFamilyRentalShortList() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.FAMILY)
                .rentals(new ArrayList<>())
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Rentals list was too short",
                    e.getResponseBodyAsString().contains("rentals length must between 3 and 5"));
        }
    }

    @Test
    public void createFamilyRentalWithNoRentalsList() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.FAMILY)
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Rentals was not present",
                    e.getResponseBodyAsString().contains("parameters sent do not match with family type"));
        }
    }

    @Test
    public void createFamilyRentalWithAmountIncluded() {
        CreateRentalRequest rentalRequest = CreateRentalRequest
                .builder()
                .type(RentalType.FAMILY)
                .amount(3)
                .rentals(buildRentalRequestList())
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", "Response status don't match"),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount should not be present",
                    e.getResponseBodyAsString().contains("parameters sent do not match with family type"));
        }
    }

    @Test
    public void getRental() {
        CreateRentalRequest rentalRequest = buildDummyRentalRequest();
        Rental rental = postRental(rentalRequest).getBody();

        ResponseEntity<Rental> getResponse =  getRental(rental.getRentalId());
        Assert.assertEquals("Response status doesn't match", HttpStatus.OK, getResponse.getStatusCode());
        Assert.assertEquals("Rental amount doesn't match",
                rentalRequest.getAmount(), getResponse.getBody().getAmount());
    }

    @Test
    public void getRentalNotFound() {
        try {
            getRental("invalid_id");
            Assert.fail("Did not throw");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Response status doesn't match", HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    private CreateRentalRequest buildDummyRentalRequest() {
        return CreateRentalRequest
                .builder()
                .type(RentalType.DAILY)
                .amount(2)
                .build();
    }

    private List<CreateRentalRequest> buildRentalRequestList() {
        CreateRentalRequest singleRental = buildDummyRentalRequest();
        List<CreateRentalRequest> rentals = new ArrayList<>();
        rentals.add(singleRental);
        rentals.add(singleRental);
        rentals.add(singleRental);
        return rentals;
    }
}
