package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.service.ShopService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    @ResponseBody
    public ResponseEntity<List<GameResponseDto>> getShop() {
        return ResponseUtils.ok(shopService.getUnownedGames());
    }

    @GetMapping("/{gameId}")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    @ResponseBody
    public ResponseEntity<GameResponseDto> getGameInShop(
            @PathVariable Integer gameId
    ) {
        return ResponseUtils.ok(shopService.getUnownedGameById(gameId));
    }

    @PostMapping("/{gameId}/buy")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    @ResponseBody
    public ResponseEntity<Void> buyGame(
            @PathVariable Integer gameId
    ) {
        shopService.buyGame(gameId);
        return ResponseUtils.noContent();
    }

}
