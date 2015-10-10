package br.com.mrcsfelipe.markfulldroid.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import br.com.mrcsfelipe.markfulldroid.model.Person;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonsModel {
	
	
	private List<Person> persons = new ArrayList<>();
	
	
	public PersonsModel() {
		
	}
	

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
	
}
