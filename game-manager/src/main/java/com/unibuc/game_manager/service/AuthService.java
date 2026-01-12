package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.CredentialsDto;
import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.dto.DeveloperDto;
import com.unibuc.game_manager.dto.PublisherDto;
import com.unibuc.game_manager.dto.TokenDto;
import com.unibuc.game_manager.exception.ForbiddenException;
import com.unibuc.game_manager.exception.ValidationException;
import com.unibuc.game_manager.mapper.CustomerMapper;
import com.unibuc.game_manager.mapper.DeveloperMapper;
import com.unibuc.game_manager.mapper.PublisherMapper;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.model.User;
import com.unibuc.game_manager.repository.CustomerRepository;
import com.unibuc.game_manager.repository.DeveloperRepository;
import com.unibuc.game_manager.repository.PublisherRepository;
import com.unibuc.game_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PublisherRepository publisherRepository;
    private final DeveloperRepository developerRepository;
    private final JWTService jwtService;
    private final CustomerMapper customerMapper;
    private final PublisherMapper publisherMapper;
    private final DeveloperMapper developerMapper;

    public TokenDto signin(CredentialsDto credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());

        if (user == null || !JWTService.isPasswordValid(credentials.getPassword(), user.getPassword())) {
            throw new ValidationException("Invalid username or password");
        }
        if (user instanceof Provider provider) {
            String message = switch (provider.getStatus()) {
                case BANNED -> "Banned account";
                case PENDING -> "Account awaiting approval";
                case REJECTED -> "Account rejected";
                default -> null;
            };
            if (message != null) throw new ForbiddenException(message);
        }

        return new TokenDto(jwtService.getToken(String.valueOf(user.getId())));
    }

    public Customer signupCustomer(CustomerDto customer) {
        return customerRepository.save(customerMapper.toEntity(customer));
    }

    public Publisher registerPublisher(PublisherDto publisher) {
        return publisherRepository.save(publisherMapper.toEntity(publisher));
    }

    public Developer registerDeveloper(DeveloperDto developer) {
        return developerRepository.save(developerMapper.toEntity(developer));
    }

}