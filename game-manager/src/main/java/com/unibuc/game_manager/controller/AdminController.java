package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireAdmin;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.AdminService;
import com.unibuc.game_manager.service.DeveloperService;
import com.unibuc.game_manager.service.PublisherService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DeveloperService developerService;
    private final PublisherService publisherService;

    @GetMapping("/developers")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<List<Developer>> getDevelopers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name
    ) {
        return ResponseUtils.ok(developerService.getAll(status, name));
    }
    @PutMapping("/developers/{developerId}/{action}")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<Developer> changeStatusDevelopers(
            @PathVariable Integer developerId,
            @PathVariable String action
    ) {
        return ResponseUtils.ok(developerService.changeStatus(developerId, action));
    }

    @GetMapping("/publishers")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<List<Publisher>> getPublishers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name
    ) {
        return ResponseUtils.ok(publisherService.getAll(status, name));
    }

    @PutMapping("/publishers/{publisherId}/{action}")
    @JsonView(ViewUtils.Admin.class)
    @RequireAdmin
    @ResponseBody
    public ResponseEntity<Publisher> changeStatusPublisher(
            @PathVariable Integer publisherId,
            @PathVariable String action
    ) {
        return ResponseUtils.ok(publisherService.changeStatus(publisherId, action));
    }

}
