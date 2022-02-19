package org.sid.demospringcloudstreamskafka.web;

import org.sid.demospringcloudstreamskafka.entities.PageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController   // c'est un Rest Controller
public class PageEventRestController {

    /* 1er chose : creer une methode permet de publier une page Event nommé publish,
 c a d publier un évenement dans un Topic Kafka c pour cela on va utiliser
 @GetMapping("/publish/{nom-du-Topic}/{nom-du-page}")
       2e chose : pour publier les messages dans Topic Kafka (on a 3 facon de le faire),
       mais on va choisi la 2e facon "Spring cloud Stream function", alors j'utilise un objet
       qui s'appelle StreamBridge qu'on va injecter par : @Authowired,
       et avec StreamBridge va fonctionner avec n'importe quelle broker mais il suffit
       d'utiliser streamBridge.send(on specifier le topic , et le message qu'on souhaite envoye avec nom de la page),
        alors pour recuper ces parametre a partir de la methode publish c par : @PathVariable
       pour le message envoye (pour envoye un msg oblige de faire ca):
c'est un objet de type PageEvent et donne a ce objet (le nom de la page et le nom d'utilisateur, et la date du system, et la dure)
        une fois que j'envoie le messsage je doit le retourner au navigateur " return"
        */
  @Autowired
  private StreamBridge streamBridge;
   @GetMapping("/publish/{topic}/{name}")
    public PageEvent publish(@PathVariable String topic,@PathVariable String name){
      PageEvent pageEvent=new PageEvent(name,Math.random()>0.5?"U1":"U2",new Date(),new Random().nextInt(9000));
       streamBridge.send(topic,pageEvent);
       return pageEvent;
   }
}