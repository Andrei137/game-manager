package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.dto.ContractDto;
import com.unibuc.game_manager.model.Contract;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.ContractService;
import com.unibuc.game_manager.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @Operation(
            summary = "Get all contracts. Provider-only operation",
            description = "Returns all contracts for the authenticated provider",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contracts retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Contract.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("")
    @RequireProvider({Developer.class, Publisher.class})
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseUtils.ok(contractService.getAllContracts());
    }

    @Operation(
            summary = "Issue a contract",
            description = "Creates a new contract for a game. Publisher-only operation",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Contract issued successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Contract.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Game at id 2 already under contract\" }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Game with id 3 not found\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/{gameId}")
    @RequireProvider({Publisher.class})
    public ResponseEntity<Contract> issueContract(
            @RequestBody @Valid ContractDto contractDto,
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.created(contractService.issueContract(contractDto, gameId));
    }

    @Operation(
            summary = "Update a contract",
            description = "Updates an existing contract. Publisher-only operation",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contract updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Contract.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Contract no longer pending, no changes allowed\" }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Contract no longer pending, no changes allowed\" }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Contract not found with game 4\" }"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{gameId}")
    @RequireProvider({Publisher.class})
    public ResponseEntity<Contract> updateContract(
            @RequestBody @Valid ContractDto contractDto,
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.ok(contractService.updateContract(contractDto, gameId));
    }

    @Operation(
            summary = "Delete a contract",
            description = "Deletes a contract by game ID. Publisher-only operation",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Contract deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Cannot delete accepted contract\" }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contract not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Contract not found with game 3\" }"
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{gameId}")
    @RequireProvider({Publisher.class})
    public ResponseEntity<Void> deleteContract(
            @PathVariable Integer gameId
    ) {
        contractService.deleteContract(gameId);
        return ResponseUtils.noContent();
    }

}
