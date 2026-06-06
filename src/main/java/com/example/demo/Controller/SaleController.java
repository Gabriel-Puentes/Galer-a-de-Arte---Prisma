package com.example.demo.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Model.Sale;
import com.example.demo.Service.ArtworkService;
import com.example.demo.Service.SaleService;


//indica que es una clase controladora
@Controller
//define una ruta base para el controlador
@RequestMapping("/venta")
public class SaleController {

//Autowired inyecta una dependencia para crear objetos
//se crean objetos para manejar los services de las entidades

    @Autowired
    private SaleService saleService;
    @Autowired
    private ArtworkService artworkService;

    // POST /venta/registrar → registra una venta nueva
    //peticion post y ruta
    @PostMapping("/registrar")
    //requestparam obtiene los parametros enviados
    public String save(@RequestParam String buyer, @RequestParam String mail, @RequestParam LocalDate date, @RequestParam List<Integer> artworkIds) {

        try{    
            saleService.registerSale(buyer, mail, date, artworkIds); //guarda la venta
            return "redirect:/?message=venta-saved"; //redirige y muestra un mensaje de confirmacion exitosa
        }catch(IllegalArgumentException e){
            return "redirect:/?message=venta-" + e.getMessage(); //si falla, redirige y muestra un mensaje de error
        }catch(IllegalStateException a){
            return "redirect:/?message=venta-" + a.getMessage(); //si falla, redirige y muestra un mensaje de error
        }
    }

    // GET /venta/buscar → busca ventas por correo
    //peticion get y ruta
    @GetMapping("/buscar")
    //requestparam obtiene los parametros enviados
    //model envia datos al frontend 
    public String searchByMail(@RequestParam String mail, Model model) {
       
        try {
            List<Sale> sales = saleService.findByMail(mail); //crea una instancia de ventas y le asigna todas aquelleas ventas asociadas al correo especificado
            model.addAttribute("salesByMail", sales); //envía las ventas encontradas a la vista
            model.addAttribute("detailsByMail", saleService.findDetailsBySales(sales)); //busca los detalles de las ventas encontradas y los hace visibles en el frontend
        } catch (IllegalArgumentException e) {
            return "redirect:/?message=venta-" + e.getMessage(); //si falla, redirige y muestra un mensaje de error
        }
        
        model.addAttribute("sales", saleService.findAllSales()); //envía el historial de ventas a la vista
        model.addAttribute("artwork", artworkService.findBySoldFalse()); //envia las obras a la vista, siempre que no estén vendidas
        return "index";
    }
}
