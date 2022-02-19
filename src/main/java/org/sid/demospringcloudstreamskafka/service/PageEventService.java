package org.sid.demospringcloudstreamskafka.service;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.sid.demospringcloudstreamskafka.entities.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service //car c'est un service
public class PageEventService {
    /*
    je vais creer des methodes,
    pour pouvoir creer un consomateur je dois creer une methode qui retourne
    une fonction consumer, alors il va consomer des objets de type PageEvent,
    et cette fonction recoit en Input(lire un enregistrement a partir du topic Kafka)
    et comme traitement je vais juste affiche le message Tostring qui contient tous
    les attributs du PageEvent
    et pour que cette methode soit deployer on doit ajouter un @Bean
    alors : quand on utilise SpringcloudStreams(fait un subscribe vers Topic automatiquement)
     */
    @Bean  // pour deployer cette fonction
    public Consumer<PageEvent> pageEventConsumer() {
        return (input) -> {
            System.out.println("*******************");
            System.out.println(input.toString());
            System.out.println("*******************");
        };
    }

    @Bean // pour deployer cette fonction
    public Supplier<PageEvent> pageEventSupplier() {
        return () -> new PageEvent(
                Math.random() > 0.5 ? "P1" : "P2",
                Math.random() > 0.5 ? "U1" : "U2",
                new Date(),
                new Random().nextInt(9000));
    }

    ;

    // maitenant une fonction qui fait le role des 2 methodes qui sont en haut.
    // il recoit un flus en entree , il fait le traitement, et il va produire un flux en sortie
    @Bean //car ont est besoin de deployer cette fonction
    public Function<PageEvent, PageEvent> pageEventFunction() {
        return (input) -> { //voila Traitement
            input.setName("Page Event"); //je modifie le nom
            input.setUser("TARHINE"); //et le user
            return input;
        };
    }

    // une fonction nomme KStreamFunction,
    // qui recoit en input un KStream (la cle : est un String, la valeur : est un PageEvent)
    // et on Output c'est aussi un KStream  (la cle : est un String(nom de page), la valeur : est un LONG(nbr de fois la page est visite))
    // il return en input un KStream , et on outPut retourne une resultat c a d 1 er chose il fait
    // le traitement de flux (d'abord filtrer les donnes mais que les evenements de visite de page dans la duree de visite depasse 100 ms)
    // puis faire un Map c a d produire un Stream(dans laquelle la cle c le nom de page et la valeur est null) en sortie
    // apres GroupBayKey(nom de page) , apres un count() pour compter
    // enfin je retourne la resultat sous forme d'un Stream .ToStream();
    @Bean
    public Function<KStream<String,PageEvent>, KStream<String,Long>> kStreamFunction(){
        return (input)->{
            return input
                    .filter((k,v)->v.getDuration()>100)
                    .map((k,v)->new KeyValue<>(v.getName(),0L))
                    .groupBy((k,v)->k,Grouped.with(Serdes.String(),Serdes.Long()))
                    .count()
                    .toStream();
        };
    }
}

