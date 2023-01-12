package com.practice.sns.dto.response;

import com.practice.sns.domain.constant.UserRole;
import com.practice.sns.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponseDto {

    private Long id;
    private String userName;
    private UserRole role;

    public static UserJoinResponseDto from(UserDto userDto) {
        return new UserJoinResponseDto(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getUserRole()
        );
    }

}
