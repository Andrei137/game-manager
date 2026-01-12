package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.dto.ContractDto;
import com.unibuc.game_manager.model.Contract;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.ContractService;
import com.unibuc.game_manager.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("")
    @RequireProvider({Developer.class, Publisher.class})
    @ResponseBody
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseUtils.ok(contractService.getAllContracts());
    }

    @PostMapping("")
    @RequireProvider({Publisher.class})
    @ResponseBody
    public ResponseEntity<Contract> issueContract(
            @RequestBody @Valid ContractDto contractDto
    ) {
        return ResponseUtils.created(contractService.issueContract(contractDto));
    }

    @PutMapping("")
    @RequireProvider({Publisher.class, Developer.class})
    @ResponseBody
    public ResponseEntity<Contract> updateContract(
            @RequestBody @Valid ContractDto contractDto
    ) {
        return ResponseUtils.ok(contractService.updateContract(contractDto));
    }

    @DeleteMapping("")
    @RequireProvider({Publisher.class})
    @ResponseBody
    public ResponseEntity<Void> deleteContract(
            @RequestBody @Valid ContractDto contractDto
    ) {
        contractService.deleteContract(contractDto);
        return ResponseUtils.noContent();
    }
}
