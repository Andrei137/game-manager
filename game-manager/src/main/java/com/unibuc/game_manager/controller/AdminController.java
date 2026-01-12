package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireAdmin;
import com.unibuc.game_manager.dto.ProviderResponseDto;
import com.unibuc.game_manager.dto.ProviderUpdateDto;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.service.AdminService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Get providers",
            description = "Returns a list of providers. Admin-only operation.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of providers retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProviderResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/providers")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<List<ProviderResponseDto>> getPublishers(
            @Parameter(description = "Filter by provider status", required = false)
            @RequestParam(required = false) String status,

            @Parameter(description = "Filter by provider name", required = false)
            @RequestParam(required = false) String name
    ) {
        return ResponseUtils.ok(adminService.getProviders(status, name));
    }

    @Operation(
            summary = "Change provider status",
            description = "Changes the status of a provider. Admin-only operation.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Provider status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Provider.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Provider with id 13 not found\" }"
                                    ),
                                    @ExampleObject(
                                            value = "{ \"error\": \"Cannot change status from accepted to rejected\" }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/providers/{providerId}")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<Provider> changeStatusPublisher(
            @PathVariable Integer providerId,
            @RequestBody @Valid ProviderUpdateDto providerUpdateDto
    ) {
        return ResponseUtils.ok(adminService.changeProviderStatus(providerId, providerUpdateDto.getStatus()));
    }

}
