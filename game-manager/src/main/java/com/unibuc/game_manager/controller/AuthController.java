package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.dto.CredentialsDto;
import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.dto.DeveloperDto;
import com.unibuc.game_manager.dto.PublisherDto;
import com.unibuc.game_manager.dto.TokenDto;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.AuthService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Sign in",
            description = "Authenticates a user with username and password. Returns a JWT token on success\""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI0In0...\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Invalid username or password\" }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Banned account",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"You are banned\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<TokenDto> signin(@RequestBody @Valid CredentialsDto credentials) {
        return ResponseUtils.ok(authService.signin(credentials));
    }

    @Operation(
            summary = "Sign up as a customer",
            description = "Registers a new customer account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"username is required and cannot be blank\" }"
                                    ),
                                    @ExampleObject(
                                            value = "{ \"error\": \"phoneNumber must be exactly 10 digits\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Customer> signupClient(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) CustomerDto customerDto
    ) {
        return ResponseUtils.created(authService.signupCustomer(customerDto));
    }

    @Operation(
            summary = "Request developer account",
            description = "Request access to a new developer account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Developer request submitted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Developer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Duplicate entry 'developer@test.com'\" }"
                                    ),
                                    @ExampleObject(
                                            value = "{ \"error\": \"website must be a valid URL\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/request/developer")
    @ResponseBody
    public ResponseEntity<Developer> requestDeveloper(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) DeveloperDto developerDto
    ) {
        return ResponseUtils.created(authService.registerDeveloper(developerDto));
    }

    @Operation(
            summary = "Request publisher account",
            description = "Request access to a new publisher account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Publisher request submitted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Publisher.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Duplicate entry 'publisher@test.com'\" }"
                                    ),
                                    @ExampleObject(
                                            value = "{ \"error\": \"website must be a valid URL\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/request/publisher")
    @ResponseBody
    public ResponseEntity<Publisher> requestPublisher(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) PublisherDto publisherDto
    ) {
        return ResponseUtils.created(authService.registerPublisher(publisherDto));
    }

}