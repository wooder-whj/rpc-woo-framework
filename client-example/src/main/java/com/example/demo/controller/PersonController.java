package com.example.demo.controller;

import com.example.demo.bean.Person;
import com.example.demo.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rpc.woo.framework.annotation.Caller;
import rpc.woo.framework.annotation.Remote;

import java.util.ArrayList;
import java.util.List;

@RestController
@Caller
public class PersonController {
    Logger logger= LoggerFactory.getLogger(PersonController.class);

    @Remote
    PersonService service;

    @GetMapping("/person")
    public Object person(){
        Person person = service.getPersonByName("李四");
        logger.info(person.toString());
        List<Person> persons = service.getPersonsByGender("女");
        persons.iterator().forEachRemaining(person1->{
            logger.info(person1.toString());
        });
        Person[] people = service.getPersonsByAge(18);
        for(Person ps:people){
            logger.info(ps.toString());
            for(String phone:ps.getPhone()){
                logger.info(phone);
            }
        }
        List<String> names=new ArrayList<>();
        names.add("宝玉");
        names.add("贾琏");
        List<Person> personList = service.findPersonsByNames(names);
        logger.info(personList.toString());
        return personList;
    }

    @GetMapping("/person/{name}")
    public Object personByName(@PathVariable("name")String name){
        Person person = service.getPersonByName(name);
        logger.info(name+"= "+person.toString());
        return person;
    }

    @PostMapping(value="/person/names")
    public Object findPersonsByNames(@RequestBody List<String> names){
        List<Person> persons = service.findPersonsByNames(names);
        logger.info(names+"= "+persons.toString());
        return persons;
    }
}
