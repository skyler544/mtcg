package org.mtcg.application.service;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;
import org.mtcg.application.model.UserProfile;
import org.mtcg.application.repository.UserRepository;
import org.mtcg.http.ForbiddenException;
import org.mtcg.http.UnauthorizedException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) throws IllegalStateException {
        return userRepository.findUserByUsername(username);
    }

    public void saveCredentials(Credentials credentials) throws IllegalStateException {
        userRepository.saveCredentials(credentials);
    }

    public String authenticateViaCredentials(Credentials credentials) throws IllegalStateException, UnauthorizedException {
        User user = findUserByUsername(credentials.getUsername());
        if (user.getCredentials().getPassword().equals(credentials.getPassword())) {
            return user.getToken();
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    public boolean authenticate(String username, String token) throws IllegalStateException {
        return findUserByUsername(username).getToken().equals(token);
    }

    public boolean adminAuthenticate(String token) throws IllegalStateException {
        return findUserByUsername("admin").getToken().equals(token);
    }

    public void saveUserProfile(String token, String username, UserProfile userProfile) throws UnauthorizedException {
        if (authenticate(username, token)) {
            userRepository.saveUserProfile(token, userProfile);
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    public UserProfile findUserProfile(String token, String username) throws UnauthorizedException {
        if (authenticate(username, token)) {
            return userRepository.findUserProfile(token);
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    // this should only be done by the admin, so we expect the admin's token here
    public void setUserCoins(String token, String username, int coins) throws UnauthorizedException {
        if (adminAuthenticate(token)) {
            // now get the token of the user whose coins we want to set
            userRepository.saveUserCoins(username, coins);
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }

    // the normal user will be allowed to do this, because they're the ones who buy
    // packages
    public void subtractUserCoins(String token, String username, int price)
            throws UnauthorizedException, ForbiddenException {
        if (authenticate(username, token)) {
            int balance = userRepository.getUserCoins(token) - price;
            if (balance < 0) {
                throw new ForbiddenException("Not enough coins.");
            } else {
                userRepository.saveUserCoins(token, balance);
            }
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }
}
