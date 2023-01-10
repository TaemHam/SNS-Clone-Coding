package com.practice.sns.fixture;

import com.practice.sns.domain.User;

public class UserEntityFixture {

    public static User get(String userName, String password) {
        return User.of(userName, password);
    }
}
