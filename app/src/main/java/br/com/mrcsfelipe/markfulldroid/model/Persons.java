package br.com.mrcsfelipe.markfulldroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markFelipe on 06/10/15.
 */
public class Persons {

    private List<Person> persons = new ArrayList<>();


    public Persons() {

    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

}
