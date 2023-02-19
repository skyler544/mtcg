package org.mtcg.application.service;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;
import org.mtcg.application.model.UserProfile;
import org.mtcg.application.repository.UserRepository;
import org.mtcg.http.exception.NotFoundException;
import org.mtcg.http.exception.UnauthorizedException;

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
            throws IllegalStateException, UnauthorizedException {
        User user = findUserByUsername(credentials.getUsername());
        if (user.getCredentials().getPassword().equals(credentials.getPassword())) {
            return user.getToken();
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    public void authenticateToken(String token) throws IllegalStateException, UnauthorizedException {
        if (findUserByToken(token) == null) {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    public void authenticate(String username, String token) throws IllegalStateException, UnauthorizedException {
        if (!findUserByUsername(username).getToken().equals(token)) {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    public void adminAuthenticate(String token) throws IllegalStateException {
        if (!findUserByUsername("admin").getToken().equals(token)) {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    public void saveUserProfile(String token, String username, UserProfile userProfile) throws UnauthorizedException {
        authenticate(username, token);
        userRepository.saveUserProfile(token, userProfile);
    }

    public UserProfile findUserProfile(String token, String username) throws UnauthorizedException, NotFoundException {
        authenticate(username, token);
        UserProfile userProfile = userRepository.findUserProfile(token);
        if (userProfile != null) {
            return userProfile;
        } else {
            throw new NotFoundException("User not found.");
        }
    }

    // this should only be done by the admin, so we expect the admin's token here
    public void setUserCoins(String token, String username, int coins) throws UnauthorizedException {
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
