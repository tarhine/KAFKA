package org.sid.demospringcloudstreamskafka.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class PageEvent {
    // les Attributs :
    // page event est definie par le nom de la page visite
    // le nom de visiteur
    // date de visite de la page
    // la durer passer dans la page
    private String name;
    private String user;
    private Date date;
    private long duration;
}
