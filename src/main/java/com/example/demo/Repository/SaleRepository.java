package com.example.demo.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Sale;

// Indica que es un repositorio
@Repository
//Extiende a JPA repositorio y permite usar los CRUD
public interface SaleRepository extends JpaRepository<Sale, Integer>{
    //retorna todas las ventas asociadas a ese correo
    List<Sale> findByMail(String mail);
}
