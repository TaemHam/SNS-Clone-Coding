package com.practice.sns.service;

import com.practice.sns.domain.User;
import com.practice.sns.dto.UserDto;
import com.practice.sns.exception.ErrorCode;
import com.practice.sns.exception.SnsApplicationException;
import com.practice.sns.repository.UserRepository;
import com.practice.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public UserDto loadUserByUserName(String userName) {
        return userRepository.findByUserName(userName).map(UserDto::from).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));
    }

    @Transactional
    public UserDto join(String userName, String password) {
        // userName 중복 체크
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
                    String.format("User Name %s is duplicated", userName));
        });

        // 회원가입 진행
        User savedUser = userRepository.save(User.of(userName, encoder.encode(password)));
        return UserDto.from(savedUser);
    }

    public String login(String userName, String password) {
        // 회원가입 여부 체크
        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("User Name %s does not exist", userName)));

        // 비밀번호 체크
        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }
}
