package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.service.CustomerService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{customerId}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer customerId) {
        return ResponseUtils.ok(customerService.getUserById(customerId));
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
