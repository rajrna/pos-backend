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
        return  new ResponseEntity<>(paymentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id){
        return new ResponseEntity<>(paymentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getByStatus(@PathVariable PaymentStatus status){
        return new ResponseEntity<>(paymentService.findByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<Payment>> getByMethod(@PathVariable PaymentMethod method){
        return new ResponseEntity<>(paymentService.findByMethod(method), HttpStatus.OK);
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> process(@RequestBody @Valid PaymentRequest request){
        return new ResponseEntity<>(paymentService.processPayment(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<Payment> fail(@PathVariable Long id){
        return new ResponseEntity<>(paymentService.failPayment(id), HttpStatus.OK);
    }

    @GetMapping("/invoices/order/{orderId}")
    public ResponseEntity<Invoice> getInvoiceByOrder(@PathVariable Long orderId){
        return new ResponseEntity<>(paymentService.findInvoiceByOrderId(orderId), HttpStatus.OK);
    }

    @GetMapping("/invoices/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoiceByNumber(@PathVariable String invoiceNumber){
        return new ResponseEntity<>(paymentService.findInvoiceByNumber(invoiceNumber), HttpStatus.OK);
    }
}
