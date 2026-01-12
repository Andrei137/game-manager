package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireAdmin;
import com.unibuc.game_manager.dto.ProviderResponseDto;
import com.unibuc.game_manager.dto.ProviderUpdateDto;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.service.AdminService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
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

    @GetMapping("/providers")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<List<ProviderResponseDto>> getPublishers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name
    ) {
        return ResponseUtils.ok(adminService.getProviders(status, name));
    }

    @PutMapping("/providers/{id}")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<Provider> changeStatusPublisher(
            @PathVariable Integer id,
            @RequestBody @Valid ProviderUpdateDto providerUpdateDto
    ) {
        return ResponseUtils.ok(adminService.changeProviderStatus(id, providerUpdateDto.getStatus()));
    }

}
