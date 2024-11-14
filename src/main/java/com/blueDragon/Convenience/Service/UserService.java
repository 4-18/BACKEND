package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.User.RegisterUserDto;
import com.blueDragon.Convenience.Exception.DuplicateLoginIdException;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void register(RegisterUserDto registerUserDto) {
        String username = registerUserDto.getUsername();
        
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateLoginIdException("중복된 아이디로는 가입할 수 없습니다.");
        }

        User user = User.signupBuilder()
                .username(registerUserDto.getUsername())
                .password(bCryptPasswordEncoder.encode(registerUserDto.getPassword()))
                .nickname(registerUserDto.getNickname())
                .name(registerUserDto.getName())
                .build();

        userRepository.save(user);
    }
}
