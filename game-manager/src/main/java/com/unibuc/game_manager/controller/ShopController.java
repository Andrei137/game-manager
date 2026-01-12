package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.service.ShopService;
import com.unibuc.game_manager.utils.ResponseUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @Operation(
            summary = "Get shop games. Customer-only operation",
            description = "Returns all games not owned by the authenticated customer",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Available games retrieved successfully",
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
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    public ResponseEntity<List<GameResponseDto>> getShop() {
        return ResponseUtils.ok(shopService.getUnownedGames());
    }


    @Operation(
            summary = "Get game from shop. Customer-only operation",
            description = "Returns details of a game not owned by the authenticated customer",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Game retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found or already owned",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Game at id 3 owned or doesn't exist\" }"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{gameId}")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    public ResponseEntity<GameResponseDto> getGameInShop(
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.ok(shopService.getUnownedGameById(gameId));
    }

    @Operation(
            summary = "Buy a game",
            description = "Purchases a game for the authenticated customer",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Game purchased successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Game already owned",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Game already owned\" }"
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
                    description = "Game not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"Game at id 3 not found\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/{gameId}/buy")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    public ResponseEntity<Void> buyGame(
            @PathVariable Integer gameId
    ) {
        shopService.buyGame(gameId);
        return ResponseUtils.noContent();
    }

}
