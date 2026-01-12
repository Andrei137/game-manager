package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.service.LibraryService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    public final LibraryService libraryService;

    @GetMapping("")
    @RequireCustomer
    @JsonView(ViewUtils.Public.class)
    @ResponseBody
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseUtils.ok(libraryService.getOwnedGames());
    }

    @GetMapping("/{gameId}")
    @RequireCustomer
    @JsonView(ViewUtils.Public.class)
    @ResponseBody
    public ResponseEntity<Game> getGameById(
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.ok(libraryService.getOwnedGameById(gameId));
    }


}
