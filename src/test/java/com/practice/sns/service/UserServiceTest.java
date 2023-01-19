package com.practice.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.practice.sns.domain.User;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.fixture.UserEntityFixture;
import com.practice.sns.repository.UserCacheRepository;
import com.practice.sns.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserCacheRepository userCacheRepository;


    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {

        // Given
        String userName = "userName";
        String password = "password";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypted_password");
        when(userRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password));

        // When & Then
        assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void 회원가입시_중복된_userName이_있는경우() {

        // Given
        String userName = "userName";
        String password = "password";
        User fixture = UserEntityFixture.get(userName, password);
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(User.class)));
        when(encoder.encode(password)).thenReturn("encrypted_password");
        when(userRepository.save(any())).thenReturn(Optional.of(fixture));

        // When & Then
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
                () -> userService.join(userName, password));
        assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우() {

        // Given
        String userName = "userName";
        String password = "password";
        User fixture = UserEntityFixture.get(userName, password);
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void 로그인시_userName이_없는경우() {

        // Given
        String userName = "userName";
        String password = "password";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // When & Then
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
                () -> userService.login(userName, password));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());

    }


    @Test
    void 로그인시_password가_틀린경우() {

        // Given
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";
        User fixture = UserEntityFixture.get(userName, password);
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // When & Then
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
                () -> userService.login(userName, wrongPassword));
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
