package com.ek.hk_pos.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(){
        return ResponseEntity.ok(orderService.findAll().stream()
                .map(orderService::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
//        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
        return  ResponseEntity.ok(orderService.toResponse(orderService.findById(id)));
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getByCustomer(@PathVariable Long customerId){
//        return new ResponseEntity<>(orderService.findByCustomerId(customerId), HttpStatus.OK);
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public  ResponseEntity<List<Order>> getByStatus(@PathVariable OrderStatus status){
        return ResponseEntity.ok(orderService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.toResponse(orderService.create(request)));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItem(@PathVariable Long orderId,
                                         @RequestBody @Valid OrderItemRequest request){
        return ResponseEntity.ok(orderService.toResponse(orderService.addItem(orderId,request)));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponse> removeItem(@PathVariable Long orderId,
                                            @PathVariable Long itemId){
        return ResponseEntity.ok(orderService.toResponse(orderService.removeItem(orderId, itemId)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @RequestBody OrderStatus status){
        return ResponseEntity.ok(orderService.toResponse(orderService.updateStatus(id, status)));
    }

    @DeleteMapping
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
