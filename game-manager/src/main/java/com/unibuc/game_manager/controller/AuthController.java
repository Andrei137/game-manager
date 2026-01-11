package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.dto.*;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.model.User;
import com.unibuc.game_manager.service.AuthService;
import com.unibuc.game_manager.utils.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<TokenDto> login(@RequestBody @Valid CredentialsDto credentials) {
        return ResponseUtils.ok(authService.login(credentials));
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<User> signupClient(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) CustomerDto customerDto
    ) {
        return ResponseUtils.created(authService.signupCustomer(customerDto));
    }

    @PostMapping("/request/developer")
    @ResponseBody
    public ResponseEntity<Developer> requestDeveloper(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) DeveloperDto developerDto
    ) {
        return ResponseUtils.created(authService.registerDeveloper(developerDto));
    }

    @PostMapping("/request/publisher")
    @ResponseBody
    public ResponseEntity<Publisher> requestPublisher(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) PublisherDto publisherDto
    ) {
        return ResponseUtils.created(authService.registerPublisher(publisherDto));
    }

}