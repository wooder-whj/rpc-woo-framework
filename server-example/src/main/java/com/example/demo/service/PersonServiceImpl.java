package com.example.demo.service;

import com.example.demo.bean.Person;
import rpc.woo.framework.annotation.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Reference
public class PersonServiceImpl implements PersonService {
    private static final String[] GENDER={"男","女"};
    private static final String[] MALE_NAME={"胡歌","宝玉","贾琏","周深"};
    private static final String[] FEMALE_NAME={"黛玉","晴雯","刘亦菲","李思思"};
    private static final String[] ADDR={"北京","上海","广州","深圳"};
    
    @Override
    public Person getPersonByName(String name) {
        Person person = new Person();
        person.setName(name);
        person.setAddress(ADDR[new Random().nextInt(4)]);
        person.setAge(new Random().nextInt(30));
        person.setGender(GENDER[new Random().nextInt(1)]);
        ArrayList<String> phones = new ArrayList<>();
        phones.add("Huawei P20");
        phones.add("Huawei Mate20 Plus");
        person.setPhone(phones);
        return person;
    }

    @Override
    public List<Person> getPersonsByGender(String gender) {
        List<Person> list=new ArrayList<Person>();
        if("男".equals(gender)){
            Person person = new Person();
            person.setName(MALE_NAME[new Random().nextInt(4)]);
            person.setAddress(ADDR[new Random().nextInt(4)]);
            person.setAge(new Random().nextInt(30));
            person.setGender("男");
            ArrayList<String> phones = new ArrayList<>();
            phones.add("Huawei P20");
            phones.add("Huawei Mate20 Plus");
            person.setPhone(phones);
            list.add(person);
            person = new Person();
            person.setName(MALE_NAME[new Random().nextInt(4)]);
            person.setAddress(ADDR[new Random().nextInt(4)]);
            person.setAge(new Random().nextInt(30));
            person.setGender("男");
            phones = new ArrayList<>();
            phones.add("Honor X10");
            phones.add("Honor Note 10");
            person.setPhone(phones);
            list.add(person);
        }else{
            Person person = new Person();
            person.setName(FEMALE_NAME[new Random().nextInt(4)]);
            person.setAddress(ADDR[new Random().nextInt(4)]);
            person.setAge(new Random().nextInt(30));
            person.setGender("女");
            ArrayList<String> phones = new ArrayList<>();
            phones.add("Huawei P20");
            phones.add("Huawei Mate20 Plus");
            person.setPhone(phones);
            list.add(person);
            person = new Person();
            person.setName(FEMALE_NAME[new Random().nextInt(4)]);
            person.setAddress(ADDR[new Random().nextInt(4)]);
            person.setAge(new Random().nextInt(30));
            person.setGender("女");
            phones = new ArrayList<>();
            phones.add("Honor X10");
            phones.add("Honor Note 10");
            person.setPhone(phones);
            list.add(person);
        }
        return list;
    }

    @Override
    public Person[] getPersonsByAge(Integer minAge) {
        Person person = new Person();
        person.setName(MALE_NAME[new Random().nextInt(4)]);
        person.setAddress(ADDR[new Random().nextInt(4)]);
        person.setAge(new Random().nextInt(30));
        person.setGender("男");
        ArrayList<String> phones = new ArrayList<>();
        phones.add("Huawei P20");
        phones.add("Huawei Mate20 Plus");
        person.setPhone(phones);
        Person person2 = new Person();
        person2.setName(FEMALE_NAME[new Random().nextInt(4)]);
        person2.setAddress(ADDR[new Random().nextInt(4)]);
        person2.setAge(new Random().nextInt(30));
        person2.setGender("女");
        phones = new ArrayList<>();
        phones.add("Huawei P20");
        phones.add("Huawei Mate20 Plus");
        person2.setPhone(phones);
        Person[] people={person,person2};
        return people;
    }

    @Override
    public List<Person> findPersonsByNames(List<String> names) {
        List<Person> list=new ArrayList<>();
        for(String name:names){
            Person person = new Person();
            person.setName(name);
            person.setAddress(ADDR[new Random().nextInt(4)]);
            person.setAge(new Random().nextInt(30));
            person.setGender(GENDER[new Random().nextInt(1)]);
            ArrayList<String> phones = new ArrayList<>();
            phones.add("Huawei P20");
            phones.add("Huawei Mate20 Plus");
            person.setPhone(phones);
            list.add(person);
        }
        return list;
    }
}
