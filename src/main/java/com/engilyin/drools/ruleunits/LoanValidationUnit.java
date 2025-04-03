package com.engilyin.drools.ruleunits;

import java.math.BigDecimal;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import com.engilyin.drools.model.LoanApplication;

import lombok.Getter;

@Getter
public class LoanValidationUnit implements RuleUnitData {
	private final DataStore<LoanApplication> applications;
	private final BigDecimal minAge = new BigDecimal("18");
	private final BigDecimal maxLoanAmount = new BigDecimal("1000000");
	private final BigDecimal minIncomeRatio = new BigDecimal("0.3");

	public LoanValidationUnit() {
		this.applications = DataSource.createStore();
	}

	public void addApplication(LoanApplication application) {
		applications.add(application);
	}
}
