package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Artwork;

// Indica que es un repositorio
@Repository 


//Extiende a JPA repositorio y permite usar los CRUD
public interface ArtworkRepository extends JpaRepository<Artwork, Integer> { 
//Spring genera el SQL automáticamente basándose en el nombre del método
    //Busca obras que no estén vendidas
    List<Artwork> findBySoldFalse();

    //Se genera un Query manualmente debido a la cantidad de fitros que utiliza
    //busca por nombre, artista (sin importar las mayusculas) tipo, también filtra por rango de precios y solo muestra obras que no estén vendidas
    @Query("SELECT a FROM Artwork a WHERE " +
           "(LOWER(a.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.artist) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.type) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
           "AND a.price BETWEEN :min AND :max " +
           "AND a.sold = false")

    //se genera el metodo y se usa la consulta 
    //Param accede al atributo usado en la columna
    List<Artwork> searchAvailable(@Param("filter") String filter, @Param("min") double min, @Param("max") double max);
    //Revisa si existen obras con un nombre y artista especificos
    boolean existsByNameAndArtist(String name, String artist);
}