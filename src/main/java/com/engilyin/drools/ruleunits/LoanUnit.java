/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.engilyin.drools.ruleunits;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import com.engilyin.drools.model.LoanApplication2;

public class LoanUnit implements RuleUnitData {

    private int maxAmount;

    private DataStore<LoanApplication2> loanApplications;

    public LoanUnit() {
        this(DataSource.createStore());
    }

    public LoanUnit(DataStore<LoanApplication2> loanApplications) {
        this.loanApplications = loanApplications;
    }

    public DataStore<LoanApplication2> getLoanApplications() {
        return loanApplications;
    }

    public void setLoanApplications(LoanApplication2 loanApplications) {
        this.loanApplications.add(loanApplications);
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

}
