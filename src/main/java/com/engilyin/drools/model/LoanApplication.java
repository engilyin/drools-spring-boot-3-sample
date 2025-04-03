package com.engilyin.drools.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplication {
	private String applicantName;
	private int applicantAge;
	private BigDecimal amount;
	private int term; // in months
	private BigDecimal income;
	private boolean approved;
	private String rejectionReason;
}
