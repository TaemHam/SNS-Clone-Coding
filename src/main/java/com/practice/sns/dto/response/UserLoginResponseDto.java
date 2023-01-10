package com.practice.sns.dto.response;

import com.practice.sns.domain.constant.UserRole;
import com.practice.sns.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponseDto {

    private String token;

}
