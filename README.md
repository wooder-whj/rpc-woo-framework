# rpc-woo-framework
Woo, a simple, none code invasion, user transparency,and annotation driving development RPC(Remote Procedure Call) framework composing of client and server endpoints such as Dubbo, is developed by Netty, a NIO network socket framework, and integrated with springboot architecture.
This framework similar with Dubbo accomplishes the most important function of remote procedure call from consumer, a springboot micro-service, to provider, another springboot micro-service, by transporting data using a protocol of json string encoded and decoded in utf-8.



## Figures

- [x] Easy to Use
- [x] Code Non-invasion
- [x] Annotation Driven
- [x] User Insensitive
- [x] Gapless Integrated with Springboot
- [x] Asynchronous Call
- [x] Netty Based
- [x] More ...



## Usage

### Client usage

1. Install all repositories in the direcotry *./woo-client/repository/* in your application repository;

2. Introduce dependency *client-spring-boot-starter* in your project *pom.xml* as below:

   ```xml
   <dependency>
       <groupId>rpc.woo.framework</groupId>
       <artifactId>client-spring-boot-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

   

3. Add annotation *@Caller* on your @Controller program to indicate that it would use a remote procedure call service.

4. Inject a remote procedure call service by marking an annotation *@Remote* over the interface service field of the controller program.

### Server usage

1. Install all repositories in the direcotry *./woo-server/repository/* in your application repository;

2. Introduce dependency *server-spring-boot-starter* in your project *pom.xml* as below:

   ```xml
   <dependency>
       <groupId>rpc.woo.framework</groupId>
       <artifactId>server-spring-boot-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

   

3. Add annotation *@Reference* on your service implementation program to indicate that it should be a service interface implentation called by a remote client.

## Directory Description
##### woo-server
This directory includes the source codes of woo framework server. 
##### server-example
This directory demos the usage of woo framework server.
##### woo-client
This directory contains all source codes of woo framework client.
##### client-example
This directory illustrates how to use the woo framework client performing remote procedure call.