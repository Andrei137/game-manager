package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.dto.ContractDto;
import com.unibuc.game_manager.dto.GameCreateDto;
import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.dto.GameUpdateDto;
import com.unibuc.game_manager.model.Contract;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.GameService;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @Operation(
            summary = "Get provider games",
            description = "Returns all games owned by the currently authenticated provider",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of providers retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponseDto.class)
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
    public ResponseEntity<List<GameResponseDto>> getAllGames(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String title
    ) {
        return ResponseUtils.ok(gameService.getAllGamesOfCurrentProvider(status, title));
    }

    @Operation(
            summary = "Announce a new game",
            description = "Creates a new game announcement. Developer-only operation.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Game announced successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"title is required and cannot be blank\" }"
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
    @PostMapping("")
    @JsonView(ViewUtils.Provider.class)
    @RequireProvider({Developer.class})
    public ResponseEntity<GameResponseDto> announceGame(
            @RequestBody @Valid GameCreateDto gameCreateDto
    ) {
        return ResponseUtils.created(gameService.announceGame(gameCreateDto));
    }

    @Operation(
            summary = "Update game",
            description = "Updates an existing game owned by the provider.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Game updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"status must be one of DEVELOPED, PUBLISHED, DELISTED\" }"
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
                                            value = "{ \"error\": \"Cannot update the game anymore, you are under contract\" }"
                                    ),
                                    @ExampleObject(
                                            value = "{ \"error\": \"Game is not developed yet\" }"
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
                                            value = "{ \"error\": \"Game with id 3 not found\" }"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{gameId}")
    @JsonView(ViewUtils.Provider.class)
    @RequireProvider({Developer.class, Provider.class})
    public ResponseEntity<GameResponseDto> updateGame(
            @PathVariable Integer gameId,
            @RequestBody @Valid GameUpdateDto gameUpdateDto
    ) {
        return ResponseUtils.created(gameService.updateGame(gameId, gameUpdateDto));
    }

    @Operation(
            summary = "Get game contracts",
            description = "Returns all contracts associated with a game. Developer-only operation.",
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Not enough permissions\" }"
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
                                            value = "{ \"error\": \"Game with id 3 not found\" }"
                                    )
                            }
                    )
            )
    })
    @GetMapping({"/{gameId}/contracts"})
    @JsonView(ViewUtils.Provider.class)
    @RequireProvider({Developer.class})
    public ResponseEntity<List<Contract>> getContracts(
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.ok(gameService.getGameContracts(gameId));
    }

    @Operation(
            summary = "Accept or update contract",
            description = "Updates the status of a contract for a specific publisher. Developer-only operation.",
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Not enough permissions\" }"
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
                                            value = "{ \"error\": \"No contract found for game id 3 and publisher id 2\" }"
                                    )
                            }
                    )
            )
    })
    @PutMapping({"/{gameId}/contracts/{publisherId}"})
    @JsonView(ViewUtils.Provider.class)
    @RequireProvider({Developer.class})
    public ResponseEntity<Contract> acceptContract(
            @PathVariable Integer gameId,
            @PathVariable Integer publisherId,
            @RequestBody @Valid @Validated(ValidationUtils.Update.class) ContractDto contractDto
    ) {
        return ResponseUtils.ok(gameService.updateContractStatus(gameId, publisherId, contractDto));
    }

}
