package com.ek.hk_pos.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer findById(Long id){
        return  customerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Customer not found with id: "+ id));
    }

    public List<Customer> search(String query){
        return  customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }

    public Customer create(CustomerRequest request){
        if (customerRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User with email already exists: "+ request.getEmail());
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        return customerRepository.save(customer);
    }

    public Customer update(Long id, CustomerRequest request){
        Customer existing = findById(id);

        if(!existing.getEmail().equals(request.getEmail()) &&
            customerRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());

        return customerRepository.save(existing);
    }

    public void delete(Long id){
        findById(id);
        customerRepository.deleteById(id);
    }

}
