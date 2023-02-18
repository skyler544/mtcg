package org.mtcg.application.repository;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.UserProfile;

public interface UserRepository {
    void saveCredentials(Credentials credentials) throws IllegalStateException;
    String findUserByUsername(String username) throws IllegalStateException;

    void saveUserProfile(String token, UserProfile userProfile) throws IllegalStateException;
    UserProfile findUserProfile(String token) throws IllegalStateException;

    void saveUserCoins(String token, int coins) throws IllegalStateException;
    int getUserCoins(String token) throws IllegalStateException;
}
