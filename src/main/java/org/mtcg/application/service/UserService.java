package org.mtcg.application.service;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;
import org.mtcg.application.model.UserProfile;
import org.mtcg.application.repository.UserRepository;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.exception.MtcgException;
import org.mtcg.http.exception.MtcgException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) throws IllegalStateException {
        return userRepository.findUserByUsername(username);
    }

    public User findUserByToken(String token) throws IllegalStateException {
        return userRepository.findUserByToken(token);
    }

    public void saveCredentials(Credentials credentials) throws IllegalStateException {
        userRepository.saveCredentials(credentials);
    }

    public String authenticateViaCredentials(Credentials credentials)
            throws IllegalStateException, MtcgException {
        User user = findUserByUsername(credentials.getUsername());
        if (user.getCredentials().getPassword().equals(credentials.getPassword())) {
            return user.getToken();
        } else {
            throw new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED);
        }
    }

    public void authenticateToken(String token) throws IllegalStateException, MtcgException {
        if (findUserByToken(token) == null) {
            throw new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED);
        }
    }

    public void authenticate(String username, String token) throws IllegalStateException, MtcgException {
        User user = findUserByUsername(username);
        if (user == null || !user.getToken().equals(token)) {
            throw new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED);
        }
    }

    public void adminAuthenticate(String token) throws IllegalStateException {
        if (!findUserByUsername("admin").getToken().equals(token)) {
            throw new MtcgException("Authentication failure.", HttpStatus.UNAUTHORIZED);
        }
    }

    public void saveUserProfile(String token, String username, UserProfile userProfile) throws MtcgException {
        authenticate(username, token);
        userRepository.saveUserProfile(token, userProfile);
    }

    public UserProfile findUserProfile(String token, String username) throws MtcgException {
        authenticate(username, token);
        UserProfile userProfile = userRepository.findUserProfile(token);
        if (userProfile == null) {
            throw new MtcgException("User not found.", HttpStatus.NOT_FOUND);
        }
        return userProfile;
    }

    // this should only be done by the admin, so we expect the admin's token here
    public void setUserCoins(String token, String username, int coins) throws MtcgException {
        adminAuthenticate(token);
        // now get the token of the user whose coins we want to set
        userRepository.saveUserCoins(username, coins);
    }

    public int checkBalance(String token) {
        return userRepository.getUserCoins(token);
    }

    public void subtractUserCoins(String token, int price) {
        userRepository.saveUserCoins(token, checkBalance(token) - price);
    }
}
