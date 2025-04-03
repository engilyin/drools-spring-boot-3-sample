package com.engilyin.drools.services;

import java.util.ArrayList;
import java.util.List;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.springframework.stereotype.Service;

import com.engilyin.drools.model.LoanApplication;
import com.engilyin.drools.model.Person;
import com.engilyin.drools.ruleunits.LoanValidationUnit;
import com.engilyin.drools.ruleunits.PersonValidationUnit;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleUnitProvider ruleUnitProvider;

    public Person validatePerson(Person person) {
        PersonValidationUnit personUnit = new PersonValidationUnit();
        personUnit.addPerson(person);
        
        try (RuleUnitInstance<PersonValidationUnit> instance = ruleUnitProvider.createRuleUnitInstance(personUnit)) {
            instance.fire();
            return person;
        }
    }
    
    public List<String> getPersonValidationMessages(Person person) {
        PersonValidationUnit personUnit = new PersonValidationUnit();
        personUnit.addPerson(person);
        List<String> messages = new ArrayList<>();
        
        try (RuleUnitInstance<PersonValidationUnit> instance = ruleUnitProvider.createRuleUnitInstance(personUnit)) {
            instance.fire();
            
            //XXX fix it
            //personUnit.getValidationMessages().forEach(messages::add);
            return messages;
        }
    }

    public LoanApplication validateLoanApplication(LoanApplication application) {
        LoanValidationUnit loanUnit = new LoanValidationUnit();
        loanUnit.addApplication(application);
        
        try (RuleUnitInstance<LoanValidationUnit> instance = ruleUnitProvider.createRuleUnitInstance(loanUnit)) {
            instance.fire();
            return application;
        }
    }
}