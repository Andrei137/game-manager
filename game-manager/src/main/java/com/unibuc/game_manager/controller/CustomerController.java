package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.service.CustomerService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseUtils.ok(customerService.getAllUsers());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        return ResponseUtils.ok(customerService.getUserById(id));
    }

    @GetMapping("/me")
    @ResponseBody
    @RequireCustomer
    public ResponseEntity<Customer> getCurrentCustomer() {
        return ResponseUtils.ok(customerService.getCurrentUser());
    }

    @PutMapping("")
    @ResponseBody
    @RequireCustomer
    public ResponseEntity<Customer> updateCustomer(@RequestBody @Validated(Update.class) CustomerDto customerDto) {
        return ResponseUtils.ok(customerService.updateLoggedUser(customerDto));
    }

}
