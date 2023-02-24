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
import org.mtcg.http.exception.MtcgException;

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
        // Act
        Response response = restCardController.addPackage(requestContext);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
    }

    @Test
    void testAddPackageFailure() {
        // Arrange
        boolean thrown = false;
        doThrow(new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED)).when(userService).adminAuthenticate(null);
        // Act
        try {
            restCardController.addPackage(requestContext);
        } catch (MtcgException e) {
            thrown = true;
        }
        // Assert
        assertTrue(thrown);
    }

    @Test
    void testAcquirePackageSuccessfully() {
        // Arrange
        when(userService.checkBalance(null)).thenReturn(20);

        // Act
        Response response = restCardController.acquirePackage(requestContext);

        //Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testAcquirePackageFailure() {
        // Arrange
        boolean thrown = false;
        when(userService.checkBalance(null)).thenReturn(0);

        // Act
        try {
            restCardController.acquirePackage(requestContext);
        } catch (MtcgException e) {
            thrown = true;
        }

        // Assert
        assertTrue(thrown);
    }

    @Test
    void testGetUserCardsSuccessfully() {
        // Arrange
        when(cardService.returnUserCards(null)).thenReturn("foo");

        // Act
        Response response = restCardController.getCards(requestContext);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testGetUserCardsFailure() {
        // Arrange
        boolean thrown = false;
        doThrow(new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED)).when(userService).authenticateToken(null);

        // Act
        try {
            restCardController.getCards(requestContext);
        } catch (MtcgException e) {
            thrown = true;
        }
        // Assert
        assertTrue(thrown);
    }

    @Test
    void testSetDeckSuccessfully() {
        // Arrange
        String[] result = {"foo", "boo", "bar", "baz"};
        when(cardService.cardIdArray(null)).thenReturn(result);

        // Act
        Response response = restCardController.setDeck(requestContext);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testSetDeckFailure() {
        // Arrange
        boolean thrown = false;
        String[] result = {"foo", "bar", "baz"};
        when(cardService.cardIdArray(null)).thenReturn(result);

        // Act
        try {
            restCardController.setDeck(requestContext);
        } catch (MtcgException e) {
            thrown = true;
        }

        // Assert
        assertTrue(thrown);
    }

    @Test
    void testGetDeckSuccessfully() {
        // Act
        Response response = restCardController.getDeck(requestContext);
        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testGetDeckFailure() {
        // Arrange
        boolean thrown = false;
        doThrow(new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED)).when(userService).authenticateToken(null);

        // Act
        try {
            restCardController.getDeck(requestContext);
        } catch (MtcgException e) {
            thrown = true;
        }

        // Assert
        assertTrue(thrown);
    }

    @Test
    void testGetDeckPlainTextSuccessfully() {
        // Act
        Response response = restCardController.getDeckPlainText(requestContext);
        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testGetDeckPlainTextFailure() {
        // Arrange
        boolean thrown = false;
        doThrow(new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED)).when(userService).authenticateToken(null);

        // Act
        try {
            restCardController.getDeckPlainText(requestContext);
        } catch (MtcgException e) {
            thrown = true;
        }

        // Assert
        assertTrue(thrown);
    }
}
