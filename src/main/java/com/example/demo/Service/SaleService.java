package com.example.demo.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.Artwork;
import com.example.demo.Model.Sale;
import com.example.demo.Model.SaleDetail;
import com.example.demo.Repository.ArtworkRepository;
import com.example.demo.Repository.SaleDetailRepository;
import com.example.demo.Repository.SaleRepository;

//Aqui se trata la lógica del negocio 

//indica que es un service
@Service

public class SaleService {

//Autowired inyecta una dependencia para crear objetos
//Se crean objetos para manejar los repositorios

    @Autowired 
    private SaleRepository saleRepository; //repositoty de las ventas

    @Autowired
    private SaleDetailRepository saleDetailRepository; //repository de los detalles de las ventas

    @Autowired
    private ArtworkRepository artworkRepository; //repository de las obras 

    //registra una nueva venta
    public Sale registerSale(String buyer, String mail, LocalDate date, List<Integer> artworkIds){

        List<Artwork> artworks = new ArrayList<>(); //variable que guarda obras encontradas
        double subtotal = 0; //guarda el subtotal de la compra
        double total; //guarda el total de la compra
        double discount = 0; //almacena el descuento

        //valida si se seleccionaron obras para comprar 
        if (artworkIds == null || artworkIds.isEmpty()){
            throw new IllegalArgumentException("Debes seleccionar al menos una obra");  //captura un error por argumento inválido y crea mensaje
        }

        //recorre todas las obras seleccionadas en la compra
        for (Integer artworkId : artworkIds) {
            Artwork artwork = artworkRepository.findById(artworkId).get(); //asigna a la variable el objeto obra encontrada con la id actual
            
            //valida si la obra ya fue vendida
            if (artwork.getSold()) {
                throw new IllegalStateException("La obra " + artwork.getName() + " ya fue vendida"); //captura un error por el estado del objeto y crea un mensaje
            }
            //añade las obras seleccionadas a la lista de obras encontradas
            artworks.add(artwork);
        }
        
        //recorre cada obra seleccionada
        for (Artwork artwork: artworks) {
            subtotal += artwork.getPrice(); //accede al precio de todas las obras y lo suma al subtotal
        }

        //cuenta el número de obras seleccionadas en la compra y compara si es igual o mayor a 3
        if (artworks.size() >= 3){
            discount = subtotal*0.1; //en caso de que el numero de obras seleccionadas sea igual o mayor a 3 se asigna un descuento correspondiente al 10% del subtotal
        }
        //revisa si el valor del subtotal excede los 3 mil millones
        if (subtotal > 3000000000L){
            discount = subtotal*0.15; //si se cumple la condicion se sobreescribe el valor descuento como el 15% del subtotal
        }

        total = subtotal - discount; //asigna el valor al total como la diferencia entre el subtotal y el descuento

        //crea el objeto venta con los valores enviados y los calculados anteriormente
        Sale sale = new Sale(buyer, mail, date, subtotal, discount, total);
        saleRepository.save(sale); //usa el metodo del repository de venta para guardar la venta
 
        //vuelve a recorrer cada obra encontrada
        for (Artwork artwork: artworks) {
            artwork.setSold(true); //marca las obras seleccionadas como vendidas
            artworkRepository.save(artwork); //actualiza la información de las obras guardandolas 
            SaleDetail detail = new SaleDetail(sale, artwork, artwork.getPrice()); //crea tantos detalles de venta como obras existen
            saleDetailRepository.save(detail); //guarda los detalles de venta con el metodo del repository detalles de venta
        }
        
        return sale;
    }

    //busca todas las ventas existentes
    public List<Sale> findAllSales(){
        return saleRepository.findAll();
    }

    //busca las ventas filtrando por correo
    public List<Sale> findByMail(String mail) {
    
        List<Sale> sales = saleRepository.findByMail(mail); //crea una lista de las ventas asociadas al correo 
        //valida si la lista está vacía
        if (sales.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron ventas con ese correo");  //captura un error por argumento inválido y crea mensaje
        }
        return sales;
    }

    //busca los detalles de multiples ventas
    public List<SaleDetail> findDetailsBySales(List<Sale> sales) {
        return saleDetailRepository.findBySaleIn(sales);
    }
}
