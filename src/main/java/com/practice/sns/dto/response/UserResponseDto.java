package com.practice.sns.dto.response;

import com.practice.sns.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public
class UserResponseDto {
    private Long id;
    private String userName;

    public static UserResponseDto from(UserDto user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername()
        );
    }

}

