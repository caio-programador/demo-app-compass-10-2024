package com.example.demo;


import com.example.demo.web.dtos.ClientCreateDTO;
import com.example.demo.web.dtos.ClientResponseDTO;
import com.example.demo.web.dtos.UserResponseDTO;
import com.example.demo.web.exceptions.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients/client-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/client-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {
        @Autowired
        WebTestClient testClient;

        @Test
        public void createClient_ValidData_ReturnsClient201(){
            var responseBody = testClient.post().uri("/api/v1/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient,"toby@gmail.com", "123456"))
                    .bodyValue(new ClientCreateDTO("Toby legal", "19893705002"))
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(ClientResponseDTO.class)
                    .returnResult().getResponseBody();
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        }

    @Test
    public void createClient_ExistedCPF_ReturnsErrorMessage409(){
        var responseBody = testClient.post().uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDTO("Toby legal", "26805583080"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void createClient_InvalidData_ReturnsErrorMessage422(){
        var responseBody = testClient.post().uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDTO("vaui", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        responseBody = testClient.post().uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDTO("", "12345678910"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createClient_NotPermittedUser_ReturnsErrorMessage403(){
        var responseBody = testClient.post().uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .bodyValue(new ClientCreateDTO("Toby legal", "26805583080"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void getClient_ExistedId_ReturnsClientResponseDTO200(){
        var responseBody = testClient.get().uri("/api/v1/clients/11")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getClient_NonExistedId_ReturnsErrorMessage404(){
        var responseBody = testClient.get().uri("/api/v1/clients/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }


    @Test
    public void getClient_LoggedLikeAClient_ReturnsErrorMessage403(){
        var responseBody = testClient.get().uri("/api/v1/clients/11")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void getAllClients_WithAdminRole_ReturnsPageOfClients200(){
        var responseBody = testClient.get().uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Object.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }


    @Test
    public void getAllClients_WithClientRole_ReturnsErrorMessage403(){
        var responseBody = testClient.get().uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


    @Test
    public void getDetails_WithClientRole_Returns200(){
        var responseBody = testClient.get().uri("/api/v1/clients/details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getDetails_WithAdminRole_ReturnsErrorMessage403(){
        var responseBody = testClient.get().uri("/api/v1/clients/details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

}
