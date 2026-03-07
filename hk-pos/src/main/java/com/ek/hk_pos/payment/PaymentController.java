package com.ek.hk_pos.payment;

import com.ek.hk_pos.invoice.Invoice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAll(){
//        return  new ResponseEntity<>(paymentService.findAll(), HttpStatus.OK);
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getByStatus(@PathVariable PaymentStatus status){
//        return new ResponseEntity<>(paymentService.findByStatus(status), HttpStatus.OK);
        return ResponseEntity.ok(paymentService.findByStatus(status));
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<Payment>> getByMethod(@PathVariable PaymentMethod method){
        return ResponseEntity.ok(paymentService.findByMethod(method));
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> process(@RequestBody @Valid PaymentRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.processPayment(request));
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<Payment> fail(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.failPayment(id));
    }

    @GetMapping("/invoices/order/{orderId}")
    public ResponseEntity<Invoice> getInvoiceByOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(paymentService.findInvoiceByOrderId(orderId));
    }

    @GetMapping("/invoices/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoiceByNumber(@PathVariable String invoiceNumber){

        return ResponseEntity.ok(paymentService.findInvoiceByNumber(invoiceNumber));
    }
}
