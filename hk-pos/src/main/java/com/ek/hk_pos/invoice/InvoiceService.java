package com.ek.hk_pos.invoice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice findInvoiceByOrderId(Long orderId){
        return invoiceRepository.findByOrderId(orderId)
                .orElseThrow(()-> new RuntimeException("Inovoice not found"));
    }

    public Invoice findInvoiceByNumber(String invoiceNumber){
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(()->new RuntimeException("Invoice not found: "+invoiceNumber));
    }
}
