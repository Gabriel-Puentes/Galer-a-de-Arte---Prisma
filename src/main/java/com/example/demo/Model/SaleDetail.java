package com.example.demo.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//representa una clase en la base de datos
@Entity
//indica que es una tabla y apunta a ella con el nombre
@Table(name = "sale_detail")
public class SaleDetail {

    //indica la key, que es id
    @Id
    //aumenta el valor de id automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //Establece la relacion entre las entidades de la tabla, muchos a uno
    //varios detalles pueden pertenecer a una misma venta
    @ManyToOne
    //mapea el atributo con el valor de la columna de la entidad referenciada 
    @JoinColumn(name = "sale_id") //columna FK que referencia la tabla sale
    private Sale sale;

    //Establece la relacion entre las entidades de la tabla, uno a uno
    //cada detalle corresponde a exactamente una obra, y una obra solo puede venderse una vez
    @OneToOne
    //mapea el atributo con el valor de la columna de la entidad referenciada 
    @JoinColumn(name = "artwork_id") // columna FK que referencia la tabla artwork
    private Artwork artwork;

    @Column(name = "unit_price")
    private double unitPrice;

    //constructor vacío
    public SaleDetail(){

    }

    //constructor con sus atributos
    public SaleDetail(Sale sale, Artwork artwork, double unitPrice){

        this.sale = sale;
        this.artwork = artwork;
        this.unitPrice = unitPrice;
    }

    //metodos getter y setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    
}
