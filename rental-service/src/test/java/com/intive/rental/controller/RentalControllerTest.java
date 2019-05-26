package com.intive.rental.controller;

import com.intive.rental.dto.FamilyRental;
import com.intive.rental.dto.SimpleRental;
import com.intive.rental.dto.constant.RentalType;
import com.intive.rental.dto.request.CreateFamilyRentalRequest;
import com.intive.rental.dto.request.CreateSimpleRentalRequest;
import com.intive.rental.service.repository.FamilyRentalRepository;
import com.intive.rental.service.repository.SimpleRentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SimpleRentalRepository repository;

    @Autowired
    private FamilyRentalRepository familyRepository;

    @MockBean
    private KafkaProducer<String, SimpleRental> rentalMessageProducer;

    @MockBean
    private KafkaProducer<String, FamilyRental> familyRentalMessageProducer;

    @Value("${baseUrl}")
    private String BASE_URL;

    @Value("${message.responseStatus}")
    private String RESPONSE_STATUS_MESSAGE;

    @Value("${message.notThrown}")
    private String NOT_THROWN_MESSAGE;

    @Before
    public void setUp() {
        repository.deleteAll();
        familyRepository.deleteAll();
        doReturn(null).when(rentalMessageProducer).send(any());
        doReturn(null).when(familyRentalMessageProducer).send(any());
    }

    private ResponseEntity<SimpleRental> postRental(CreateSimpleRentalRequest createRentalRequest) {
        return restTemplate.postForEntity(String.format(BASE_URL + "/simple", port),
                createRentalRequest, SimpleRental.class);
    }

    private ResponseEntity<FamilyRental> postFamilyRental(CreateFamilyRentalRequest createFamilyRentalRequest) {
        return restTemplate.postForEntity(String.format(BASE_URL + "/family", port),
                createFamilyRentalRequest, FamilyRental.class);
    }

    private ResponseEntity<SimpleRental> getRental(String rentalId) {
        return restTemplate.getForEntity(String.format(BASE_URL + "/simple/{rentalId}", port),
                SimpleRental.class, rentalId);
    }

    private ResponseEntity<FamilyRental> getFamilyRental(String rentalId) {
        return restTemplate.getForEntity(String.format(BASE_URL + "/family/{rentalId}", port),
                FamilyRental.class, rentalId);
    }

    @Test
    public void getPort() {
        Assert.assertThat("Should get a random port greater than zero!", port, greaterThan(0));
    }

    @Test
    public void createSimpleRental() {
        CreateSimpleRentalRequest rentalRequest = buildDummyRentalRequest();
        ResponseEntity<SimpleRental> response = postRental(rentalRequest);

        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.CREATED, response.getStatusCode());
        verify(rentalMessageProducer, times(1)).send(any());
    }

    @Test
    public void hourlyRentalCost() {
        CreateSimpleRentalRequest rentalRequest = buildDummyRentalRequest();
        ResponseEntity<SimpleRental> response = postRental(rentalRequest);

        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.CREATED, response.getStatusCode());
        Assert.assertEquals("Hourly rental cost is not right", new Integer(20), response.getBody().getCost());
    }

    @Test
    public void dailyRentalCost() {
        CreateSimpleRentalRequest rentalRequest = buildDummyRentalRequest();
        rentalRequest.setRentalType(RentalType.DAILY);
        rentalRequest.setAmount(1);
        ResponseEntity<SimpleRental> response = postRental(rentalRequest);

        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.CREATED, response.getStatusCode());
        Assert.assertEquals("Daily rental cost is not right", new Integer(20), response.getBody().getCost());
    }

    @Test
    public void weeklyRentalCost() {
        CreateSimpleRentalRequest rentalRequest = buildDummyRentalRequest();
        rentalRequest.setRentalType(RentalType.WEEKLY);
        rentalRequest.setAmount(1);
        ResponseEntity<SimpleRental> response = postRental(rentalRequest);

        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.CREATED, response.getStatusCode());
        Assert.assertEquals("Weekly rental cost is not right", new Integer(60), response.getBody().getCost());
    }

    @Test
    public void createRentalNullValuesRequest() {
        CreateSimpleRentalRequest rentalRequest = CreateSimpleRentalRequest
                .builder()
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail(NOT_THROWN_MESSAGE);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", RESPONSE_STATUS_MESSAGE),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount is required and was not present",
                    e.getResponseBodyAsString().contains("amount is required"));
            Assert.assertTrue("Type is required and was not present",
                    e.getResponseBodyAsString().contains("rentalType is required"));
        }
    }

    @Test
    public void createRentalLowAmountRequest() {
        CreateSimpleRentalRequest rentalRequest = CreateSimpleRentalRequest
                .builder()
                .rentalType(RentalType.DAILY)
                .amount(-2)
                .build();
        try {
            postRental(rentalRequest);
            Assert.fail(NOT_THROWN_MESSAGE);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", RESPONSE_STATUS_MESSAGE),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Amount was too low",
                    e.getResponseBodyAsString().contains("amount should be greater than 0"));
        }
    }

    @Test
    public void createFamilyRental() {
        CreateFamilyRentalRequest familyRentalRequest = CreateFamilyRentalRequest
                .builder()
                .rentals(buildRentalRequestList())
                .build();
        ResponseEntity<FamilyRental> response = postFamilyRental(familyRentalRequest);

        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.CREATED, response.getStatusCode());
        Assert.assertEquals("Family rental cost is not right", new Integer(70), response.getBody().getCost());
    }

    @Test
    public void createFamilyRentalShortList() {
        CreateFamilyRentalRequest familyRentalRequest = CreateFamilyRentalRequest
                .builder()
                .rentals(new ArrayList<>())
                .build();
        try {
            postFamilyRental(familyRentalRequest);
            Assert.fail(NOT_THROWN_MESSAGE);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", RESPONSE_STATUS_MESSAGE),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("Rentals list was too short",
                    e.getResponseBodyAsString().contains("rentals length must between 3 and 5"));
        }
    }

    @Test
    public void createFamilyRentalWithNoRentalsList() {
        CreateFamilyRentalRequest familyRentalRequest = CreateFamilyRentalRequest
                .builder()
                .build();
        try {
            postFamilyRental(familyRentalRequest);
            Assert.fail(NOT_THROWN_MESSAGE);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(String.format("Custom constrain validation: %s", RESPONSE_STATUS_MESSAGE),
                    HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assert.assertTrue("rentals is required and was not present",
                    e.getResponseBodyAsString().contains("rentals is required"));
        }
    }

    @Test
    public void getSimpleRental() {
        CreateSimpleRentalRequest rentalRequest = buildDummyRentalRequest();
        SimpleRental rental = postRental(rentalRequest).getBody();

        ResponseEntity<SimpleRental> getResponse =  getRental(rental.getRentalId());
        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.OK, getResponse.getStatusCode());
        Assert.assertEquals("SimpleRental amount doesn't match",
                rentalRequest.getAmount(), getResponse.getBody().getAmount());
    }

    @Test
    public void getRentalNotFound() {
        try {
            getRental("invalid_id");
            Assert.fail(NOT_THROWN_MESSAGE);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void getFamilyRental() {
        CreateFamilyRentalRequest rentalRequest = CreateFamilyRentalRequest
                .builder()
                .rentals(buildRentalRequestList())
                .build();
        FamilyRental rental = postFamilyRental(rentalRequest).getBody();

        ResponseEntity<FamilyRental> getResponse =  getFamilyRental(rental.getRentalId());
        Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.OK, getResponse.getStatusCode());
        Assert.assertEquals("rentals size doesn't match",
                rentalRequest.getRentals().size(), getResponse.getBody().getRentals().size());
    }

    @Test
    public void getFamilyRentalNotFound() {
        try {
            getFamilyRental("invalid_id");
            Assert.fail(NOT_THROWN_MESSAGE);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(RESPONSE_STATUS_MESSAGE, HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    private CreateSimpleRentalRequest buildDummyRentalRequest() {
        return CreateSimpleRentalRequest
                .builder()
                .amount(4)
                .rentalType(RentalType.HOURLY)
                .build();
    }

    private List<CreateSimpleRentalRequest> buildRentalRequestList() {
        CreateSimpleRentalRequest hourlyRental = buildDummyRentalRequest();
        CreateSimpleRentalRequest dailyRental = buildDummyRentalRequest();
        dailyRental.setAmount(1);
        dailyRental.setRentalType(RentalType.DAILY);
        CreateSimpleRentalRequest weeklyRental = buildDummyRentalRequest();
        weeklyRental.setAmount(1);
        weeklyRental.setRentalType(RentalType.WEEKLY);
        List<CreateSimpleRentalRequest> rentals = new ArrayList<>();
        rentals.add(hourlyRental);
        rentals.add(dailyRental);
        rentals.add(weeklyRental);
        return rentals;
    }
}
