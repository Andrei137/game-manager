package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.service.LibraryService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    public final LibraryService libraryService;

    @Operation(
            summary = "Get owned games",
            description = "Returns all games owned by the currently authenticated customer.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Owned games retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Game.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping("")
    @RequireCustomer
    @JsonView(ViewUtils.Public.class)
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseUtils.ok(libraryService.getOwnedGames());
    }

    @Operation(
            summary = "Get owned game by ID",
            description = "Returns a specific game owned by the currently authenticated customer.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Game retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Game.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game at id 3 not owned or doesn't exist",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{gameId}")
    @RequireCustomer
    @JsonView(ViewUtils.Public.class)
    public ResponseEntity<Game> getGameById(
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.ok(libraryService.getOwnedGameById(gameId));
    }

}
