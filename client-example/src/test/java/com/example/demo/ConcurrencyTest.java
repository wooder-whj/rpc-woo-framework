package com.example.demo;

import com.example.demo.bean.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ConcurrencyTest {
    Logger logger=LoggerFactory.getLogger(ConcurrencyTest.class);

//    @Autowired
//    ApplicationContext context;

    @Test
    public void test01(){
        String[] names={"胡歌","宝玉","贾琏","周深"};
        getPersonByName(names);
    }

    @Test
    public void test02(){
        String[] names={"胡歌2","宝玉2","贾琏2","周深2"};
        findPersonsByNames(names);
    }

    @Test
    public void test03(){
        RestTemplate restTemplate = new RestTemplate();
        String[] names={"胡歌3","宝玉3","贾琏3","周深3"};
        getPersonByName(names);
    }

    private void getPersonByName(String[] names){
        RestTemplate restTemplate = new RestTemplate();
        for(int i=0;i<10000;i++){
            try{
                String name=names[new Random().nextInt(4)];
                String response = restTemplate.getForObject("http://localhost:8082/person/{name}", String.class, name);
                logger.info(name+": "+response);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void findPersonsByNames(String[] names){
        RestTemplate restTemplate = new RestTemplate();
        for(int i=0;i<10000;i++){
            try{
                List<Person> response= restTemplate.postForObject("http://localhost:8082/person/names", Arrays.asList(names),List.class );
                logger.info(names+": "+response);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}
