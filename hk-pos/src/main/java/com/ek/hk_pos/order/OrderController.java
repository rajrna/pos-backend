package com.ek.hk_pos.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll(){
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getByCustomer(@PathVariable Long customerId){
        return new ResponseEntity<>(orderService.findByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public  ResponseEntity<List<Order>> getByStatus(@PathVariable OrderStatus status){
        return new ResponseEntity<>(orderService.findByStatus(status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid OrderRequest request){
        return new ResponseEntity<>(orderService.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItem(@PathVariable Long orderId,
                                         @RequestBody @Valid OrderItemRequest request){
        return new ResponseEntity<>(orderService.addItem(orderId, request), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Order> removeItem(@PathVariable Long orderId,
                                            @PathVariable Long itemId){
        return new ResponseEntity<>(orderService.removeItem(orderId, itemId), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestBody OrderStatus status){
        return  new ResponseEntity<>(orderService.updateStatus(id, status), HttpStatus.OK);
    }

    @DeleteMapping
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
