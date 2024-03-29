package org.mtcg.application.controller;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;
import org.mtcg.application.model.UserProfile;
import org.mtcg.application.service.UserService;
import org.mtcg.http.exception.BadRequestException;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.Response;
import org.mtcg.http.exception.UnauthorizedException;
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

    @Test
    void testUpdateProfileSuccessfully() {
        // Arrange
        final UserProfile userProfile = new UserProfile("foo", "bar", "baz");
        final String token = "token";
        final String username = "username";
        // Act
        Response response = restUserController.updateProfile(token, username, userProfile);
        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testUpdateProfileFailure() {
        // Arrange
        boolean thrown = false;
        final UserProfile userProfile = new UserProfile("foo", "bar", "baz");
        final String token = "token";
        final String username = "username";
        doThrow(new UnauthorizedException("Authentication failure."))
                .when(userService).saveUserProfile(token, username, userProfile);
        // Act
        try {
            restUserController.updateProfile(token, username, userProfile);
        } catch (UnauthorizedException e) {
            thrown = true;
        }
        // Assert
        assertTrue(thrown);
    }

    @Test
    void getProfileSuccessfully() {
        // Arrange
        final String token = "token";
        final String username = "username";
        // Act
        Response response = restUserController.getProfile(token, username);
        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void getProfileFailure() {
        // Arrange
        boolean thrown = false;
        final String token = "token";
        final String username = "username";
        doThrow(new UnauthorizedException("Authentication failure."))
                .when(userService).findUserProfile(token, username);
        // Act
        try {
            restUserController.getProfile(token, username);
        } catch (UnauthorizedException e) {
            thrown = true;
        }
        // Assert
        assertTrue(thrown);
    }

    @Test
    void loginSuccessfully() {
        // Arrange
        final String token = "foo-mtcgToken";
        final String username = "foo";
        final String password = "bar";
        Credentials credentials = new Credentials(username, password);
        when(userService.authenticateViaCredentials(credentials)).thenReturn(token);

        // Act
        Response response = restUserController.login(credentials);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(token, response.getBody());
    }

    @Test
    void loginFailure() {
        // Arrange
        boolean thrown = false;
        final String username = "foo";
        final String password = "bar";
        Credentials credentials = new Credentials(username, password);
        when(userService.authenticateViaCredentials(credentials))
                .thenThrow(new UnauthorizedException("Authentication failure."));

        // Act
        try {
            restUserController.login(credentials);
        } catch (UnauthorizedException e) {
            thrown = true;
        }

        // Assert
        assertTrue(thrown);
    }
}
