package com.ek.hk_pos.payment;

import com.ek.hk_pos.exception.BadRequestException;
import com.ek.hk_pos.exception.DuplicateResourceException;
import com.ek.hk_pos.exception.ResourceNotFoundException;
import com.ek.hk_pos.invoice.Invoice;
import com.ek.hk_pos.invoice.InvoiceRepository;
import com.ek.hk_pos.order.Order;
import com.ek.hk_pos.order.OrderRepository;
import com.ek.hk_pos.order.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public List<Payment> findAll(){
        return paymentRepository.findAll();
    }

    public Payment findById(Long id){
        return paymentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id: "+id));
    }

    public Payment processPayment(PaymentRequest request){
        Order order = orderRepository.findById(request.getOrderId()).
                orElseThrow(()->new ResourceNotFoundException("Order not found with id: "+request.getOrderId()));

        if(order.getStatus()!= OrderStatus.CONFIRMED){
            throw new BadRequestException("Order must be CONFIRMED before payment");
        }

        if(paymentRepository.findByOrderId(order.getId()).isPresent()){
            throw new DuplicateResourceException(("Payment already exists for order: "+order.getId()));
        }

        Payment payment = Payment.builder()
                .order(order)
                .method(request.getMethod())
                .status(PaymentStatus.COMPLETED)
                .amount(order.getTotalAmount())
                .notes(request.getNotes())
                .build();

        payment = paymentRepository.save(payment);

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        generateInvoice(order, payment);

        return  payment;
    }

    public Payment failPayment(Long paymentId){
        Payment payment = findById(paymentId);
        payment.setStatus(PaymentStatus.FAILED);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }

    public List<Payment> findByStatus(PaymentStatus status){
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> findByMethod(PaymentMethod method){
        return paymentRepository.findByMethod(method);
    }

    public Invoice findInvoiceByOrderId(Long orderId) {
        return invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for order: " + orderId));
    }

    public Invoice findInvoiceByNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + invoiceNumber));
    }

    public void generateInvoice(Order order, Payment payment){
        String invoiceNumber = generateInvoiceNumber();

        Invoice invoice = Invoice.builder()
                .order(order)
                .payment(payment)
                .invoiceNumber(invoiceNumber)
                .totalAmount(order.getTotalAmount())
                .build();

        invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber(){
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = invoiceRepository.count()+1;
        return String.format("INV-%s-%05d", datePart, count);
    }

}
