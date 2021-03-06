package com.example.sumapplication.controller;

import com.example.sumapplication.containers.ConfigurationContainer;
import com.example.sumapplication.model.SumResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SumControllerTests extends ConfigurationContainer {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String HOST = "http://localhost:%s%s";
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testGetStatusCodeRequestParams_GivenNumberOneIsIntegerAndNumberTwoIsString_Should400BadRequest() throws Exception {
        // Arrange
        int numberOne = 500;
        String numberTwo = "a";
        String request = String.format("/sums/requestParam.postSum?numberOne=%s&numberTwo=%s", numberOne, numberTwo);
        // Act
        var response = restTemplate.postForEntity(String.format(HOST, port, request), null, String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetStatusCodePathVariable_GivenNumberOneIsIntegerAndNumberTwoIsString_Should400BadRequest() throws Exception {
        // Arrange
        int numberOne = 100;
        String numberTwo = "H";
        String request = String.format("/sums/pathVariable.postSum/%s/%s", numberOne, numberTwo);
        // Act
        var response = restTemplate.postForEntity(String.format(HOST, port, request), null, String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetStatusCodeRequestBody_GivenNumberOneIsIntegerAndNumberTwoIsString_Should400BadRequest() throws Exception {
        // Arrange
        int numberOne = 1000;
        String numberTwo = "K";
        String request = "/sums/requestBody.postSum";
        String jsonExpected = String.format("{\"numberOne\":%s,\"numberTwo\":%s}", numberOne, numberTwo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonExpected, headers);
        // Act
        var response = restTemplate.postForEntity(String.format(HOST, port, request), requestEntity, String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetResultSumRequestParam_GivenTeenAndFifteen_ShouldReturnTwentyFive() throws Exception {
        // Arrange
        int numberOne = 10;
        int numberTwo = 15;
        String requestUrlRequestParams = String.format("/sums/requestParam.postSum?numberOne=%s&numberTwo=%s", numberOne, numberTwo);
        SumResult sumResult = new SumResult(numberOne + numberTwo);
        String sumResultAsString = OBJECT_MAPPER.writeValueAsString(sumResult);
        // Act
        var response = restTemplate.postForEntity(String.format(HOST, port, requestUrlRequestParams), null, String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sumResultAsString);
    }

    @Test
    public void testGetResultSumPathVariable_GivenFiveAndTeen_ShouldReturnFifteen() throws Exception {
        // Arrange
        int numberOne = 5;
        int numberTwo = 10;
        SumResult sumResult = new SumResult(numberOne + numberTwo);
        String sumResultAsString = OBJECT_MAPPER.writeValueAsString(sumResult);
        String request = String.format("/sums/pathVariable.postSum/%s/%s", numberOne, numberTwo);
        // Act
        var response = restTemplate.postForEntity(String.format(HOST, port, request), null, String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sumResultAsString);
    }

    @Test
    public void testGetResultSumRequestBody_GivenElevenAndNine_ShouldReturnTwenty() throws Exception {
        // Arrange
        int numberOne = 11;
        int numberTwo = 9;
        SumResult sumResult = new SumResult(numberOne + numberTwo);
        String sumResultAsString = OBJECT_MAPPER.writeValueAsString(sumResult);
        String request = "/sums/requestBody.postSum";
        String jsonExpected = String.format("{\"numberOne\":%s,\"numberTwo\":%s}", numberOne, numberTwo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(jsonExpected, headers);
        // Act
        var response = restTemplate.postForEntity(String.format(HOST, port, request), requestEntity, String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sumResultAsString);
    }
}
