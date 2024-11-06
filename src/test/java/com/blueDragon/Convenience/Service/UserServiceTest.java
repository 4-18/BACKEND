package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.User.RegisterUserDto;
import com.blueDragon.Convenience.Exception.DuplicateLoginIdException;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @Test
    void register_SuccessfulRegistration() {
        // Given
        RegisterUserDto registerUserDto = new RegisterUserDto("testUser", "testLoginId", "testPassword");

        // Mocking: 중복된 loginId가 없다고 가정
        when(userRepository.existsByLoginId(registerUserDto.getLoginId())).thenReturn(false);

        // When
        userService.register(registerUserDto);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_DuplicateLoginIdException() {
        // Given
        RegisterUserDto registerUserDto = new RegisterUserDto("testUser", "duplicateLoginId", "testPassword");
        // Mocking: 중복된 loginId가 존재한다고 가정
        when(userRepository.existsByLoginId(registerUserDto.getLoginId())).thenReturn(true);
        // When & Then
        assertThrows(DuplicateLoginIdException.class, () -> userService.register(registerUserDto));
        verify(userRepository, never()).save(any(User.class));
    }
}