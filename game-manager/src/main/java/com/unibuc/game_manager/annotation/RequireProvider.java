package com.unibuc.game_manager.annotation;

import com.unibuc.game_manager.model.Provider;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireProvider {
    Class<? extends Provider>[] value() default {};
}
