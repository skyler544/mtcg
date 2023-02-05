package org.mtcg.application.repository;

import org.mtcg.application.model.User;

public interface UserRepository {
    void save(User user);
    User findUserByUsername(String username);
}
