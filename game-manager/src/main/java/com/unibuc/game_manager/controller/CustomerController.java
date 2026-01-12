package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireCustomer;
import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.service.CustomerService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils.Update;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Get all customers",
            description = "Returns a list of all registered customers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customers retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            )
    })
    @GetMapping("")
    @JsonView(ViewUtils.Public.class)
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseUtils.ok(customerService.getAllUsers());
    }

    @Operation(
            summary = "Get current customer",
            description = "Returns the currently authenticated customer",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current customer retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    @GetMapping("/me")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    public ResponseEntity<Customer> getCurrentCustomer() {
        return ResponseUtils.ok(customerService.getCurrentUser());
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Returns a customer by their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current customer retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"error\": \"No customer found at id 3\" }"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{customerId}")
    @JsonView(ViewUtils.Public.class)
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer customerId) {
        return ResponseUtils.ok(customerService.getUserById(customerId));
    }

    @Operation(
            summary = "Update current customer",
            description = "Updates the profile of the currently authenticated customer",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current customer upated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    @PutMapping("")
    @JsonView(ViewUtils.Public.class)
    @RequireCustomer
    public ResponseEntity<Customer> updateCustomer(@RequestBody @Validated(Update.class) CustomerDto customerDto) {
        return ResponseUtils.ok(customerService.updateLoggedUser(customerDto));
    }

}
