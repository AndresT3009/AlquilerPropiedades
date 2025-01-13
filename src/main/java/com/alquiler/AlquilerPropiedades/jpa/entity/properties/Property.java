package com.alquiler.AlquilerPropiedades.jpa.entity.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "propertyName")
    private String propertyName;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "available")
    private boolean available;

    @Column(name = "mortgageValue")
    private int mortgageValue;

    @Column(name = "urlImage")
    private String image;

    @Column(name = "date")
    private LocalDate date;
    @Column(name="isDeleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    public Property(){
    }

    public Property(String propertyName, String city, String address, boolean available, int mortgageValue, String image, LocalDate date, boolean isDeleted) {
        this.propertyName = propertyName;
        this.city = city;
        this.address = address;
        this.available = available;
        this.mortgageValue = mortgageValue;
        this.image = image;
        this.date = date;
        this.isDeleted = isDeleted;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(int mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", propertyName='" + propertyName + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", available=" + available +
                ", mortgageValue=" + mortgageValue +
                ", image='" + image + '\'' +
                ", date=" + date +
                ", isDeleted=" + isDeleted +
                ", client=" + client +
                '}';
    }
}
