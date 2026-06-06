package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Sale;
import com.example.demo.Model.SaleDetail;

// Indica que es un repositorio
@Repository
//Extiende a JPA repositorio y permite usar los CRUD
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer>{

   //busca los detalles de multiples ventas a la vez
   List<SaleDetail> findBySaleIn(List<Sale> sales); 
}
