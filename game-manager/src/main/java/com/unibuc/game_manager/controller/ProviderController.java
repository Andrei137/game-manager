package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.dto.ProviderCreateDto;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.ProviderService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProviderController<P extends Provider, D extends ProviderCreateDto> {

    public abstract ProviderService<P, D> getService();

    @Operation(
            summary = "Get all providers",
            description = "Returns all accepted providers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Providers retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Provider.class)
                    )
            )
    })
    @GetMapping("")
    @JsonView(ViewUtils.Public.class)
    public ResponseEntity<List<P>> getAll() {
        return ResponseUtils.ok(getService().getProvidersByStatus(Provider.Status.ACCEPTED));
    }

    @Operation(
            summary = "Get current provider",
            description = "Returns the currently authenticated provider",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current provider retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Provider.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    @GetMapping("/me")
    @JsonView(ViewUtils.Public.class)
    @RequireProvider({Developer.class, Publisher.class})
    public ResponseEntity<P> getCurrent() {
        return ResponseUtils.ok(getService().getCurrentUser());
    }

    @Operation(
            summary = "Get provider by ID",
            description = "Returns an accepted provider by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current provider retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Provider not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"No developer found at id 3\" }"
                                    ),
                                    @ExampleObject(
                                            value = "{ \"error\": \"No publisher found at id 3\" }"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{providerId}")
    @JsonView(ViewUtils.Public.class)
    public ResponseEntity<P> getById(@PathVariable Integer providerId) {
        return ResponseUtils.ok(getService().getProviderByIdAndStatus(providerId, Provider.Status.ACCEPTED));
    }

    @Operation(
            summary = "Update current provider",
            description = "Updates the currently authenticated provider",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current provider upated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    @PutMapping("")
    @JsonView(ViewUtils.Public.class)
    @RequireProvider({Developer.class, Publisher.class})
    public ResponseEntity<P> updateCurrent(@RequestBody @Validated(ValidationUtils.Update.class) D dto) {
        return ResponseUtils.ok(getService().updateLoggedUser(dto));
    }

}
