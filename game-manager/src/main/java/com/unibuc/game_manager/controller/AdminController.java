package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireAdmin;
import com.unibuc.game_manager.dto.ProviderUpdateDto;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.AdminService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/developers")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<List<Developer>> getDevelopers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name
    ) {
        return ResponseUtils.ok(adminService.getDevelopers(status, name));
    }
    @PutMapping("/developers/{id}")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<Developer> changeStatusDevelopers(
            @PathVariable Integer id,
            @RequestBody @Valid ProviderUpdateDto providerUpdateDto
    ) {
        return ResponseUtils.ok(adminService.changeDeveloperStatus(id, providerUpdateDto.getStatus()));
    }

    @GetMapping("/publishers")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<List<Publisher>> getPublishers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name
    ) {
        return ResponseUtils.ok(adminService.getPublishers(status, name));
    }

    @PutMapping("/publishers/{id}")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<Publisher> changeStatusPublisher(
            @PathVariable Integer id,
            @RequestBody @Valid ProviderUpdateDto providerUpdateDto
    ) {
        return ResponseUtils.ok(adminService.changePublisherStatus(id, providerUpdateDto.getStatus()));
    }

}
