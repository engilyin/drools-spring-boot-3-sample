package com.engilyin.drools.ruleunits;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import com.engilyin.drools.model.Person;

import lombok.Getter;

@Getter
public class PersonValidationUnit implements RuleUnitData {

	private final DataStore<Person> persons;
	private final DataStore<String> validationMessages;

	public PersonValidationUnit() {
		this.persons = DataSource.createStore();
		this.validationMessages = DataSource.createStore();
	}

	public void addPerson(Person person) {
		persons.add(person);
	}

	public void addValidationMessage(String message) {
		validationMessages.add(message);
	}

}
