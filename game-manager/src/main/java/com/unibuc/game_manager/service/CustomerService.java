package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.mapper.CustomerMapper;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public final class CustomerService extends UserService<Customer, CustomerDto> {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private JwtService jwtService;

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
