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

    public String findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public void persist(Credentials credentials) {
        userRepository.persist(credentials);
    }

    public boolean authenticate(String username, String token) {
        return findUserByUsername(username).equals(token);
    }

    public void persistUserProfile(String token, String username, UserProfile userProfile) {
        if (authenticate(username, token)) {
            userRepository.persistUserProfile(token, userProfile);
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
    }
}
