package com.example.demo;
// teste de ponta a ponta

import com.example.demo.web.dtos.UserCreateDTO;
import com.example.demo.web.dtos.UserPasswordDTO;
import com.example.demo.web.dtos.UserResponseDTO;
import com.example.demo.web.exceptions.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {
    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_ValidUsernameAndPassword_ReturnsCreatedUser201() {
        UserResponseDTO responseBody = testClient.post().uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("tody@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.id()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.username()).isNotNull();
    }

    @Test
    public void createUser_InvalidUsername_ReturnsErrorMessage422() {
        ErrorMessage responseBody = testClient.post().uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        responseBody = testClient.post().uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("asdasd", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_InvalidPassword_ReturnsErrorMessage422() {
        ErrorMessage responseBody = testClient.post().uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("neymar@gmail.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("neymar@gmail.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_ExistedUsername_ReturnsErrorMessage409() {
        ErrorMessage responseBody = testClient.post().uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void getUser_ExistedUserId_ReturnsUserResponseDTO200() {
        UserResponseDTO responseBody = testClient.get().uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();


        responseBody = testClient.get().uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        responseBody = testClient.get().uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getUser_NonExistedUserId_ReturnsErrorMessage404() {
        ErrorMessage responseBody = testClient.get().uri("api/v1/users/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void getUser_ClientSearchToOtherClient_ReturnsErrorMessage403() {
        ErrorMessage responseBody = testClient.get().uri("api/v1/users/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void changeUserPassword_UserIdExists_ReturnsNoContent204() {
        testClient.patch().uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "545454"))
                .exchange()
                .expectStatus().isNoContent();

        testClient.patch().uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana2@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "545454"))
                .exchange()
                .expectStatus().isNoContent();


    }

    @Test
    public void changeUserPassword_WithNoPermission_ReturnsErrorMessage403() {
        ErrorMessage responseBody = testClient.patch().uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana2@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "545454"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void changeUserPassword_WrongPassword_ReturnsErrorMessage400() {
        ErrorMessage responseBody = testClient.patch().uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123454", "545454"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void changeUserPassword_WrongNewPassword_ReturnsErrorMessage422() {
        ErrorMessage responseBody = testClient.patch().uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "54452"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        responseBody = testClient.patch().uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void getAllUser_ReturnsAListOfUserResponseDTO200() {
        var responseBody = testClient.get().uri("api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana1@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getAllUser_WithIncorrectPermission_ReturnsErrorResponse403() {
        var responseBody = testClient.get().uri("api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana2@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

}
