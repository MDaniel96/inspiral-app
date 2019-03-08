package com.inspiral.inspiralbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="entry")
public class Entry {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "trainerid")
    private Integer trainerid;

    @Column(name = "trainername")
    private String trainername;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date date;

    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "image")
    private byte[] image;



    public void updateWithNotNull(Entry other) {
        if (other.title != null) this.title = other.title;
        if (other.content != null) this.content = other.content;
        if (other.image != null) this.image = other.image;
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

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public byte[] getImage() {
        return image;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}