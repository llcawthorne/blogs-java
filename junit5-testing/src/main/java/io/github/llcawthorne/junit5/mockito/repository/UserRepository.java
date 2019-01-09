package io.github.llcawthorne.junit5.mockito.repository;

import io.github.llcawthorne.junit5.mockito.User;

public interface UserRepository {

    User insert(User user);
    boolean isUsernameAlreadyExists(String userName);

}
