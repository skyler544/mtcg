package org.mtcg.application.service;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.UserProfile;
import org.mtcg.application.repository.UserRepository;
import org.mtcg.http.UnauthorizedException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String findUserByUsername(String username) throws IllegalStateException {
        return userRepository.findUserByUsername(username);
    }

    public void persist(Credentials credentials) throws IllegalStateException {
        userRepository.persist(credentials);
    }

    public boolean authenticate(String username, String token) throws IllegalStateException {
        return findUserByUsername(username).equals(token);
    }

    public void persistUserProfile(String token, String username, UserProfile userProfile) throws UnauthorizedException {
        if (authenticate(username, token)) {
            userRepository.persistUserProfile(token, userProfile);
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
}
