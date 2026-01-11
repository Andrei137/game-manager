package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.dto.GameCreateDto;
import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.dto.GameUpdateDto;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.GameService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("")
    @RequireProvider({Developer.class, Publisher.class})
    @ResponseBody
    public ResponseEntity<List<GameResponseDto>> getAllGames(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String title
    ) {
        return ResponseUtils.ok(gameService.getAllGamesOfCurrentProvider(status, title));
    }

    @PostMapping("")
    @JsonView(ViewUtils.Provider.class)
    @RequireProvider({Developer.class})
    @ResponseBody
    public ResponseEntity<GameResponseDto> announceGame(
            @RequestBody @Valid @Validated(ValidationUtils.Create.class) GameCreateDto gameCreateDto
    ) {
        return ResponseUtils.created(gameService.announceGame(gameCreateDto));
    }

    @PutMapping("/{id}")
    @JsonView(ViewUtils.Provider.class)
    @RequireProvider({Developer.class, Provider.class})
    @ResponseBody
    public ResponseEntity<GameResponseDto> updateGame(
            @PathVariable Integer id,
            @RequestBody @Valid @Validated(ValidationUtils.Update.class) GameUpdateDto gameUpdateDto
    ) {
        return ResponseUtils.created(gameService.updateGame(id, gameUpdateDto));
    }

}
