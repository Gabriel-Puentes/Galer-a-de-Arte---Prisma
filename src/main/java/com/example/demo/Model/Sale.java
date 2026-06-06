package com.example.demo.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//representa una clase en la base de datos
@Entity
//indica que es una tabla y apunta a ella con el nombre
@Table(name = "sale")
public class Sale {

    //indica la key, que es id
    @Id
    //aumenta el valor de id automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//mapea los atributos con las columnas de la BS
//atributos de clase
    @Column(name = "buyer")
    private String buyer;

    @Column(name = "mail")
    private String mail;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "discount")
    private double discount;

    @Column(name = "total")
    private double total;

    //constructor vacío, JPA crea objetos internamente, después completa sus atributos
    public Sale() {

    }

    //constructor con los atributos de clase
    public Sale(String buyer, String mail, LocalDate date, double subtotal, double discount, double total) {

        this.buyer = buyer;
        this.mail = mail;
        this.date = date;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
    }

    //metodos getter y setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
