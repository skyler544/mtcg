package org.mtcg.application.repository;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;

public interface UserRepository {
    void persist(Credentials credentials) throws IllegalStateException;
    User findUserByUsername(String username);
}
