package com.diego.lime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "providers")
public class Provider {

    @Id
    private Long id;
    private String name;
    private String logoPath;

    protected Provider() {
    }

    public Provider(Long id, String name, String logoPath) {
        this.id = id;
        this.name = name;
        this.logoPath = logoPath;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogoPath() {return logoPath;}
}