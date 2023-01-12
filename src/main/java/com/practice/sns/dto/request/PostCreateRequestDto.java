package com.practice.sns.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequestDto {

    private String title;
    private String body;

}
