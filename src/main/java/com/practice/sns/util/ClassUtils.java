package com.practice.sns.util;

import java.util.Optional;

public class ClassUtils {

    public static <T> Optional<T> getCastInstance(Object obj, Class<T> cls) {
        return cls != null && cls.isInstance(obj) ? Optional.of(cls.cast(obj)) : Optional.empty();
    }
}
