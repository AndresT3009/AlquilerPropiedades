package com.alquiler.AlquilerPropiedades.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;
    @Column(name = "firstName")
    private String fisrtName;
    @Column(name = "secondName")
    private String secondName;
    @Column(name = "surName")
    private String surName;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "document")
    private Long document;
    @Column(name = "username")
    private String username;
    @Column (name = " password")
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Property> properties = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_roles",joinColumns = @JoinColumn(name = "client_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public Client() {
    }

    public Client(String fisrtName, String secondName, String surName, String phoneNumber, Long document) {
        this.fisrtName = fisrtName;
        this.secondName = secondName;
        this.surName = surName;
        this.phoneNumber = phoneNumber;
        this.document = document;
    }

    public Client (String username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return client_id;
    }

    public void setId(Long client_id) {
        this.client_id = client_id;
    }

    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getDocument() {
        return document;
    }

    public void setDocument(Long document) {
        this.document = document;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        property.setClient(this);
        properties.add(property);
    }

    @Override
    public String toString() {
        return "Client{" +
                "client_id=" + client_id +
                ", fisrtName='" + fisrtName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surName='" + surName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", document=" + document +
                ", properties=" + properties +
                '}';
    }
}