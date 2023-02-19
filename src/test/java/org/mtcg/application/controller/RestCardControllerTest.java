package org.mtcg.application.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mtcg.application.service.CardService;
import org.mtcg.application.service.UserService;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.RequestContext;
import org.mtcg.http.Response;
import org.mtcg.http.exception.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestCardControllerTest {
    @Mock
    private CardService cardService;
    @Mock
    private UserService userService;
    @Mock
    private RequestContext requestContext;

    @InjectMocks
    private RestCardController restCardController;

    @Test
    void testAddPackageSuccessfully() {
        // Arrange
        when(userService.adminAuthenticate(null)).thenReturn(true);
        // Act
        Response response = restCardController.addPackage(requestContext);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
    }

    @Test
    void testAddPackageFailure() {
        // Arrange
        boolean thrown = false;
        when(userService.adminAuthenticate(null)).thenThrow(new UnauthorizedException("Authentication failure."));
        // Act
        try {
            restCardController.addPackage(requestContext);
        } catch (UnauthorizedException e) {
            thrown = true;
        }
        // Assert
        assertTrue(thrown);
    }
}
