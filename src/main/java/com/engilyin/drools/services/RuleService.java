package com.engilyin.drools.services;

import java.util.List;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.springframework.stereotype.Service;

import com.engilyin.drools.model.LoanApplication;
import com.engilyin.drools.model.LoanApplication2;
import com.engilyin.drools.model.Person;
import com.engilyin.drools.ruleunits.LoanUnit;
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
        
        try (RuleUnitInstance<PersonValidationUnit> instance = ruleUnitProvider.createRuleUnitInstance(personUnit)) {
            instance.fire();
            
            return personUnit.getValidationMessages();
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
    
    public LoanApplication2 validateLoanApplication2(LoanApplication2 application) {
    	LoanUnit loanUnit = new LoanUnit();
    	loanUnit.setLoanApplications(application);
    	
    	try (RuleUnitInstance<LoanUnit> instance = ruleUnitProvider.createRuleUnitInstance(loanUnit)) {
    		instance.fire();
    		return application;
    	}
    }
}