package io.github.llcawthorne.junit5.mockito.repository;

import io.github.llcawthorne.junit5.mockito.User;

public interface MailClient {

    void sendUserRegistrationMail(User user);

}
