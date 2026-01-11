package com.unibuc.game_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GameManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameManagerApplication.class, args);
    }

}
