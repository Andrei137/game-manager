package com.unibuc.game_manager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class CredentialsDto {

    @NotBlank(message = "username is required and cannot be blank")
    private String username;

    @NotBlank(message = "password is required and cannot be blank")
    private String password;

}