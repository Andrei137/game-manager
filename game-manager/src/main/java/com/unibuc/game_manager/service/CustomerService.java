package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.mapper.CustomerMapper;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class CustomerService extends UserService<Customer, CustomerDto> {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    protected JpaRepository<Customer, Integer> getRepository() {
        return customerRepository;
    }

    @Override
    protected String getEntityName() {
        return "customer";
    }

    @Override
    protected CustomerMapper getMapper() {
        return customerMapper;
    }
}
