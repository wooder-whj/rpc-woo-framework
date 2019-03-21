package com.example.demo.service;

import com.example.demo.bean.Person;

import java.util.List;

public interface PersonService {
    Person getPersonByName(String name);
    List<Person> getPersonsByGender(String gender);
    Person[] getPersonsByAge(Integer minAge);
    List<Person> findPersonsByNames(List<String> names);
}
