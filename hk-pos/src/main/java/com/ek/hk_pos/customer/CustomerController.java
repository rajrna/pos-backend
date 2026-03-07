package com.ek.hk_pos.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAll(){
//        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> search(@RequestParam String query){
        return ResponseEntity.ok(customerService.search(query));
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody @Valid CustomerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody @Valid CustomerRequest request){
        return ResponseEntity.ok(customerService.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
