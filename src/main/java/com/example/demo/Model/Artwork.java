package com.example.demo.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//representa una clase en la base de datos
@Entity
//indica que es una tabla y apunta a ella con el nombre
@Table(name = "artwork")
public class Artwork {

    //indica la key, que es id
    @Id
    //aumenta el valor de id automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//mapea los atributos con las columnas de la BS
//atributos de clase
    @Column(name = "name")
    private String name;

    @Column(name = "artist")
    private String artist;

    @Column(name = "price")
    private double price;

    @Column(name = "sold")
    private boolean sold;

    @Column(name = "type")
    private String type;

    @Column(name = "image")
    private String image;

    //constructor vacío, JPA crea objetos internamente, después completa sus atributos
    public Artwork() {
        
    }

    //constructor con los atributos de clase, (id es un atributo que se completa automaticamente, no es necesario)
    // El id se incluye por completitud
    public Artwork(int id, String name, String artist, double price, boolean sold, String type, String image) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.price = price;
        this.sold = sold;
        this.type = type;
        this.image = image;
    }

    //metodos getter y setter

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getName() {
        return name; 
    }
    public void setName(String name) {
        this.name = name; 
    }

    public String getArtist() { 
        return artist; 
    }
    public void setArtist(String artist) {
        this.artist = artist; 
    }

    public double getPrice() { 
        return price; 
    }
    public void setPrice(double price) { 
        this.price = price; 
    }

    public boolean getSold() { 
        return sold; 
    }
    public void setSold(boolean sold) {
        this.sold = sold; 
    }

    public String getType() { 
        return type; 
    }
    public void setType(String type) {
        this.type = type; 
    }

    public String getImage() { 
        return image; 
    }
    public void setImage(String image) { 
        this.image = image; 
    }
    
}