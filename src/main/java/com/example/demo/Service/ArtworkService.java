package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.Artwork;
import com.example.demo.Repository.ArtworkRepository;

//Aqui se trata la lógica del negocio 

//indica que es un service
@Service
public class ArtworkService {

    //inyecta una dependencia para crear objetos
    @Autowired
    //comunica el repository con el service, lo habilita a usar los metodos dentro del service
    private ArtworkRepository artworkRepository;

    // Guarda una obra nueva
    public Artwork saveArtwork(Artwork artwork) {

        //valida que el precio de las obras sea mayor a 0
        if (artwork.getPrice() <= 0){
            throw new IllegalArgumentException("El precio debe ser mayor a 0"); //captura un error por argumento inválido y crea mensaje
        }
        //valida que no existan obras con el mismo nombre y artísta
        if(artworkRepository.existsByNameAndArtist(artwork.getName(), artwork.getArtist())){   
            throw new IllegalArgumentException("Ya existe esta obra"); //captura un error por argumento inválido y crea mensaje
        }
        //guarda la obra llamando al metodo en el repository
        return artworkRepository.save(artwork);
    }

    // Elimina una obra por id
    public void deleteArtwork(int id) {
        // Verifica si la obra existe antes de eliminarla
        if (!artworkRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una obra con la ID:" + id ); //captura un error por argumento inválido y crea mensaje
        }
        //Busca el objeto artwork con la key id y lo asigna a la variable existing
        Artwork existing = artworkRepository.findById(id).get();
        //revisa si la obra fue vendida
        if (existing.getSold()){
            throw new IllegalStateException("No se puede eliminar una obra que esté vendida"); //captura un error por el estado del objeto y crea un mensaje
        }
        //elimina la obra llamando al metodo en el repository
        artworkRepository.deleteById(id);
        
    }

    // Actualiza una obra existente
    public Artwork updateArtwork(int id, Artwork artwork) {
        // Verifica si la obra existe antes de actualizar
        if (!artworkRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una obra con la ID:" + id ); //captura un error por argumento inválido y crea mensaje
        }

        //Busca el objeto artwork con la key id y lo asigna a la variable existing
        Artwork existing = artworkRepository.findById(id).get();
        //revisa si la obra fue vendida
        if (existing.getSold()){
            throw new IllegalStateException("No se puede editar una obra que esté vendida"); //captura un error por el estado del objeto y crea un mensaje
        }
        //revisa si una nueva imagen fue enviada desde el frontend
        
        if (artwork.getImage()==null){
            artwork.setImage(existing.getImage()); //en caso que no se haya enviado una nueva imagen, conserva la que ya existía
        }
        //actualiza la obra llamando al metodo de guardar, del repository
        return artworkRepository.save(artwork);
    }

    //busca las obras que no estén vendidas
    public List<Artwork> findBySoldFalse(){
        return artworkRepository.findBySoldFalse();
    }

    //busca las obras por los filtros de nombre, artista o tipo, ademas de los rangos de precio
    public List<Artwork> searchAvailable( String filter, double min, double max){
        //valida que los precios ingresados no sean negativos
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Los precios no pueden ser negativos"); //captura un error por argumento inválido y crea mensaje
        }
        //valida que el valor mínimo no sea mayor al máximo
        if (min > max) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al máximo"); //captura un error por argumento inválido y crea mensaje
        }
        return artworkRepository.searchAvailable(filter, min, max);
    }

}