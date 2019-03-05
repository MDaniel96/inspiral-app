package com.inspiral.inspiralbackend.models;

import javax.persistence.*;

@Entity
@Table(name="question")
public class Question {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pollid")
    private Integer pollid;

    @Column(name = "content")
    private String content;


    public Integer getId() {
        return id;
    }

    public Integer getPollid() {
        return pollid;
    }

    public String getContent() {
        return content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPollid(Integer pollid) {
        this.pollid = pollid;
    }

    public void setContent(String content) {
        this.content = content;
    }


}