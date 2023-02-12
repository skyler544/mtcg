package org.mtcg.application.repository;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.UserProfile;

public interface UserRepository {
    void persist(Credentials credentials) throws IllegalStateException;
    String findUserByUsername(String username) throws IllegalStateException;

    void persistUserProfile(String token, UserProfile userProfile) throws IllegalStateException;
    UserProfile findUserProfile(String token) throws IllegalStateException;
}
