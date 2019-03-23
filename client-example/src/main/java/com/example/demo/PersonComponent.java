package com.example.demo;

import com.example.demo.bean.Person;
import com.example.demo.service.PersonService;
import rpc.woo.framework.annotation.Caller;
import rpc.woo.framework.annotation.Remote;

import java.util.List;

@Caller
public class PersonComponent {
    @Remote
    private PersonService personService;

    public Person getPersonByName(String name){
        return personService.getPersonByName(name);
    }

    public List<Person> getPersonsByGender(String gender){
        return personService.getPersonsByGender(gender);
    }

    public Person[] getPersonsByAge(Integer minAge){
        return personService.getPersonsByAge(minAge);
    }

    public List<Person> findPersonsByNames(List<String> names){
        return personService.findPersonsByNames(names);
    }
}
