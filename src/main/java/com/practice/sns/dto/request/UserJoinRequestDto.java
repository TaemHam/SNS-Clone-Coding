package com.practice.sns.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequestDto {

    private String userName;
    private String password;

}
