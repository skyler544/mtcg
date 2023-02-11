package org.mtcg.application.repository;

import org.mtcg.application.model.Credentials;

public interface UserRepository {
    void persist(Credentials credentials) throws IllegalStateException;
    String findUserByUsername(String username);
}
