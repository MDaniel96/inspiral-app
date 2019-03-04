package com.inspiral.inspiralbackend.models;

import javax.persistence.*;

@Entity
@Table(name="trainer")
public class Trainer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Lob
    @Column(name = "image")
    private byte[] image;


    public Trainer() {}

    public Trainer(String name, String email, String password, byte[] image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public void updateWithNotNull(Trainer other) {
        if (other.name != null) this.name = other.name;
        if (other.email != null) this.email = other.email;
        if (other.password != null) this.password = other.password;
        if (other.image != null) this.image = other.image;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public byte[] getImage(){ return this.image; }

    public void setImage(byte[] image){ this.image = image; }
}
