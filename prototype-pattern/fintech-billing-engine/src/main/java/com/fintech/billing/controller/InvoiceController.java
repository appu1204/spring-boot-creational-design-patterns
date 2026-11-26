package com.fintech.billing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.billing.dto.request.InvoiceRequest;
import com.fintech.billing.dto.response.InvoiceResponse;
import com.fintech.billing.service.NaiveInvoiceService;
import com.fintech.billing.service.PrototypeInvoiceService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("api/fintech/billing")
@RequiredArgsConstructor 
public class InvoiceController {

    private final NaiveInvoiceService naiveInvoiceService;
    private final PrototypeInvoiceService prototypeInvoiceService;
    

    @PostMapping ("/naive")
    public ResponseEntity<InvoiceResponse> generateNaive(@RequestBody InvoiceRequest request) {
        InvoiceResponse response = naiveInvoiceService.createInvoiceNaive(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping ("/prototype")
    public ResponseEntity<InvoiceResponse> generatePrototype(@RequestBody InvoiceRequest request) {
        InvoiceResponse response = prototypeInvoiceService.createInvoiceWithPrototype(request);
        return ResponseEntity.ok(response);
    }

}
