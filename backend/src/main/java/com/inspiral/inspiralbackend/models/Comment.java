package com.inspiral.inspiralbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="comment")
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "entryid")
    private Integer entryid;

    @Column(name = "username")
    private String username;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date date;



    public Integer getId() {
        return id;
    }

    public Integer getEntryid() {
        return entryid;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEntryid(Integer entryid) {
        this.entryid = entryid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
