package com.example.demo;

import com.example.demo.web.dtos.ParkingCreateDTO;
import com.example.demo.web.dtos.ParkingResponseDTO;
import com.example.demo.web.exceptions.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parking/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void checkIn_WithValidData_ReturnsParkingResponseDTO200(){
        var responseBody = testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .bodyValue(new ParkingCreateDTO(
                    "JBH0J05", "HONDA", "CIVIC", "BRANCO","26805583080"
                )).exchange()
                .expectStatus().isCreated()
                .expectBody(Object.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void checkIn_WithUserRole_ReturnsErrorMessage403(){
        var responseBody = testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .bodyValue(new ParkingCreateDTO(
                        "JBH0J05", "HONDA", "CIVIC", "BRANCO","26805583080"
                )).exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void checkIn_InvalidData_ReturnsErrorMessage422(){
        var responseBody = testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .bodyValue(new ParkingCreateDTO(
                        "JBH05", "HONDA", "CIVIC", "BRANCO","26583080"
                )).exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void checkIn_NonExistedCPF_ReturnsErrorMessage404(){
        var responseBody = testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .bodyValue(new ParkingCreateDTO(
                        "JBH0J05", "HONDA", "CIVIC", "BRANCO","66762939007"
                )).exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Sql(scripts = "/sql/parking/parking-busy-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void checkIn_WithBusyVagas_ReturnsErrorMessage404(){
        var responseBody = testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .bodyValue(new ParkingCreateDTO(
                        "JBH0J05", "HONDA", "CIVIC", "BRANCO","26805583080"
                )).exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void getCheckIn_WithAdminOrClient_ReturnsData200(){
        var responseBody = testClient.get().uri("/api/v1/parking/check-in/20230313-101500")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(Object.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getCheckIn_ReceiptNotFound_ReturnsErrorMessage404(){
        var responseBody = testClient.get().uri("/api/v1/parking/check-in/20230313-10150asd0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void checkOut_ExistedReceipt_ParkingResponseDTO200(){
        var responseBody = testClient.put().uri("/api/v1/parking/check-in/20230313-101500")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(Object.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void checkOut_NonExistedReceipt_ErrorMessage404(){
        var responseBody = testClient.put().uri("/api/v1/parking/check-in/20230313-101500dsaass")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void checkOut_ClientRole_ErrorMessage403(){
        var responseBody = testClient.put().uri("/api/v1/parking/check-in/20230313-101500")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void getParking_ExistedCpf_PageOfParkingResponseDTO200(){
        var responseBody = testClient.get().uri("/api/v1/parking/cpf/26805583080")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(Object.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getParking_RoleClient_ErrorMessage403(){
        var responseBody = testClient.get().uri("/api/v1/parking/cpf/20230313-101500")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void getParking_SpecificClient_PageParkingResponseDTO200(){
        var responseBody = testClient.get().uri("/api/v1/parking")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(Object.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getParking_RoleAdmin_ErrorMessage403(){
        var responseBody = testClient.get().uri("/api/v1/parking")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


}
