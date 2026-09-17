package com.copilot.profile.service;

import com.copilot.profile.domain.CustomerProfile;
import com.copilot.profile.dto.CustomerProfileDto;
import com.copilot.profile.repository.CustomerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Stands in for a "Customer Profile" backend service.
 */
@Service
public class CustomerProfileService {

    private final CustomerProfileRepository repository;

    public CustomerProfileService(CustomerProfileRepository repository) {
        this.repository = repository;
    }

    public CustomerProfileDto getProfile(String customerId) {
        CustomerProfile customer = repository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("No customer found with ID " + customerId));

        return new CustomerProfileDto(
                customer.getCustomerId(),
                customer.getFullName(),
                customer.getDateOfBirth(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getRiskTolerance() != null ? customer.getRiskTolerance().name() : null,
                customer.getKycStatus()
        );
    }
}
