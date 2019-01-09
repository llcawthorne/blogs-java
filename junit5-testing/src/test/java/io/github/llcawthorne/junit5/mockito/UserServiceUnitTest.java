package io.github.llcawthorne.junit5.mockito;

import io.github.llcawthorne.junit5.mockito.repository.MailClient;
import io.github.llcawthorne.junit5.mockito.repository.SettingRepository;
import io.github.llcawthorne.junit5.mockito.repository.UserRepository;
import io.github.llcawthorne.junit5.mockito.service.DefaultUserService;
import io.github.llcawthorne.junit5.mockito.service.Errors;
import io.github.llcawthorne.junit5.mockito.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceUnitTest {

    UserService userService;
    @Mock SettingRepository settingRepository;
    @Mock UserRepository userRepository;
    @Mock MailClient mailClient;

    User user;

    @BeforeEach
    void init() {
        userService = new DefaultUserService(userRepository, settingRepository, mailClient);
        when(settingRepository.getUserMinAge()).thenReturn(10);
        when(settingRepository.getUserNameMinLength()).thenReturn(4);
        when(userRepository.isUsernameAlreadyExists(any(String.class))).thenReturn(false);
    }

    @Test
    void givenValidUser_whenSaveUser_thenSucceed() {
        // Given
        user = new User("Jerry", 12);
        when(userRepository.insert(any(User.class))).then(new Answer<User>() {
            int sequence = 1;

            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                User user = (User) invocation.getArgument(0);
                user.setId(sequence++);
                return user;
            }
        });

        userService = new DefaultUserService(userRepository, settingRepository, mailClient);

        // When
        User insertedUser = userService.register(user);

        // Then
        verify(userRepository).insert(user);
        Assertions.assertNotNull(user.getId());
        verify(mailClient).sendUserRegistrationMail(insertedUser);
    }

    @Test
    void givenShortName_whenSaveUser_thenGiveShortUsernameError() {
        // Given
        user = new User("tom", 12);

        // When
        try {
            userService.register(user);
            fail("Should give an error");
        } catch(Exception ex) {
            assertEquals(ex.getMessage(), Errors.USER_NAME_SHORT);
        }

        // Then
        verify(userRepository, never()).insert(user);
    }

    @Test
    void givenSmallAge_whenSaveUser_thenGiveYoungUserError() {
        // Given
        user = new User("jerry", 3);

        // When
        try {
            userService.register(user);
            fail("Should give an error");
        } catch(Exception ex) {
            assertEquals(ex.getMessage(), Errors.USER_AGE_YOUNG);
        }

        // Then
        verify(userRepository, never()).insert(user);
    }

    @Test
    void givenUserWithExistingName_whenSaveUser_thenGiveUsernameAlreadyExistsError() {
        // Given
        user = new User("jerry", 12);
        Mockito.reset(userRepository);
        when(userRepository.isUsernameAlreadyExists(any(String.class))).thenReturn(true);

        // When
        try {
            userService.register(user);
            fail("Should give an error");
        } catch(Exception ex) {
            assertEquals(ex.getMessage(), Errors.USER_NAME_DUPLICATE);
        }

        // Then
        verify(userRepository, never()).insert(user);
    }

}
