package com.inspiral.inspiralbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="training")
public class Training {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "trainerid")
    private Integer trainerid;

    @Column(name = "trainername")
    private String trainername;

    @Column(name = "type")
    private String type;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date date;

    @Column(name = "lat")
    private Integer lat;

    @Column(name = "lon")
    private Integer lon;



    public void updateWithNotNull(Training other) {
        if (other.type != null) this.type = other.type;
        if (other.title != null) this.title = other.title;
        if (other.content != null) this.content = other.content;
        if (other.date != null) this.date = other.date;
        if (other.lat != null) this.lat = other.lat;
        if (other.lon != null) this.lon = other.lon;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTrainerid() {
        return trainerid;
    }

    public String getTrainername() {
        return trainername;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public Integer getLat() {
        return lat;
    }

    public Integer getLon() {
        return lon;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTrainerid(Integer trainerid) {
        this.trainerid = trainerid;
    }

    public void setTrainername(String trainername) {
        this.trainername = trainername;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public void setLon(Integer lon) {
        this.lon = lon;
    }
}
