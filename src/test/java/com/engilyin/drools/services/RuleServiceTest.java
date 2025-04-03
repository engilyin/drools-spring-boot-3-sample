package com.engilyin.drools.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.engilyin.drools.model.LoanApplication;
import com.engilyin.drools.model.Person;

@SpringBootTest
class RuleServiceTest {

	@Autowired
	RuleService ruleService;

	@Test
	void testPersonValidation() {
		// Test child categorization
		Person child = new Person("Child Person", 15, "test");
		ruleService.validatePerson(child);
		assertEquals("Child", child.getCategory());

		// Test adult categorization
		Person adult = new Person("Adult Person", 35, "test");
		ruleService.validatePerson(adult);
		assertEquals("Adult", adult.getCategory());

		// Test senior categorization
		Person senior = new Person("Senior Person", 70, "test");
		ruleService.validatePerson(senior);
		assertEquals("Senior", senior.getCategory());

		// Test validation messages
		List<String> messages = ruleService.getPersonValidationMessages(child);
		assertFalse(messages.isEmpty());
		assertTrue(messages.stream().anyMatch(msg -> msg.contains("Child Person")));
	}

	@Test
	void testLoanValidation() {
		// Test underage applicant (should be rejected)
		LoanApplication underageApp = LoanApplication
				.builder()
					.applicantName("Young Person")
					.applicantAge(17)
					.amount(new BigDecimal("10000"))
					.term(12)
					.income(new BigDecimal("5000"))
					.build();
		ruleService.validateLoanApplication(underageApp);
		assertFalse(underageApp.isApproved());
		assertTrue(underageApp.getRejectionReason().contains("18 years old"));

		// Test excessive loan amount (should be rejected)
		LoanApplication excessiveLoanApp = LoanApplication
				.builder()
					.applicantName("Greedy Person")
					.applicantAge(30)
					.amount(new BigDecimal("2000000"))
					.term(36)
					.income(new BigDecimal("100000"))
					.build();
		ruleService.validateLoanApplication(excessiveLoanApp);
		assertFalse(excessiveLoanApp.isApproved());
		assertTrue(excessiveLoanApp.getRejectionReason().contains("maximum allowed"));

		// Test income ratio too high (should be rejected)
		LoanApplication poorIncomeApp = LoanApplication
				.builder()
					.applicantName("Poor Person")
					.applicantAge(30)
					.amount(new BigDecimal("50000"))
					.term(36)
					.income(new BigDecimal("10000"))
					.build();
		ruleService.validateLoanApplication(poorIncomeApp);
		assertFalse(poorIncomeApp.isApproved());
		assertTrue(poorIncomeApp.getRejectionReason().contains("30% of income"));

		// Test valid application (should be approved)
		LoanApplication validApp = LoanApplication
				.builder()
					.applicantName("Valid Person")
					.applicantAge(30)
					.amount(new BigDecimal("30000"))
					.term(36)
					.income(new BigDecimal("120000"))
					.build();
		ruleService.validateLoanApplication(validApp);
		assertTrue(validApp.isApproved());
		assertNull(validApp.getRejectionReason());
	}
}
