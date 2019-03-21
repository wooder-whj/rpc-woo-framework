package com.example.demo.controller;

import com.example.demo.PersonComponent;
import com.example.demo.bean.Person;
import com.example.demo.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rpc.woo.framework.annotation.Caller;
import rpc.woo.framework.annotation.Remote;
import rpc.woo.framework.common.RpcContext;

import java.util.ArrayList;
import java.util.List;

@RestController
@Caller
public class PersonController {
    Logger logger= LoggerFactory.getLogger(PersonController.class);

    @Remote
    PersonService service;

//    @Autowired
//    RpcContext rpcContext;

    @GetMapping("/person")
    public Object person(){
//        PersonComponent service = (PersonComponent) rpcContext.getBeanByType(PersonComponent.class);
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
//        RpcClient<ProtocolBean> client = (RpcClient<ProtocolBean>)context.getBeanByType(NettyRpcClient.class);
//        client.close();
        return personList;
    }
}
