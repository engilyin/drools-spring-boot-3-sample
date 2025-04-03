package com.engilyin.drools.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engilyin.drools.model.LoanApplication;
import com.engilyin.drools.model.Person;
import com.engilyin.drools.services.RuleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @PostMapping("/validate-person")
    public ResponseEntity<Map<String, Object>> validatePerson(@RequestBody Person person) {
        Person validatedPerson = ruleService.validatePerson(person);
        List<String> messages = ruleService.getPersonValidationMessages(person);
        
        Map<String, Object> response = new HashMap<>();
        response.put("person", validatedPerson);
        response.put("messages", messages);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-loan")
    public ResponseEntity<LoanApplication> validateLoan(@RequestBody LoanApplication application) {
        LoanApplication validatedApplication = ruleService.validateLoanApplication(application);
        return ResponseEntity.ok(validatedApplication);
    }
}