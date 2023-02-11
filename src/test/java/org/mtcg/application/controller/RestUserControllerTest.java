package org.mtcg.application.controller;

import org.mtcg.application.model.User;
import org.mtcg.application.model.Credentials;
import org.mtcg.application.service.UserService;
import org.mtcg.http.BadRequestException;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestUserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private RestUserController restUserController;

    @Test
    void testRegisterUserSuccessfully() {
        // Arrange
        final var credentials = new Credentials("foo", "bar");

        // Act
        Response response = restUserController.register(credentials);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
    }

    @Test
    void testRegisterExistingUserFailure() {
        // Arrange
        boolean thrown = false;
        final var credentials = new Credentials("foo", "bar");
        when(userService.findUserByUsername(credentials.getUsername()))
            .thenReturn(new User(credentials));

        // Act
        try {
            restUserController.register(credentials);
        } catch (BadRequestException e) {
            thrown = true;
        }

        // Assert
        assertTrue(thrown);
    }
}
