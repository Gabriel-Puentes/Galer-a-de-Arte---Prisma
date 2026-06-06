package com.example.demo.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Model.Artwork;
import com.example.demo.Service.ArtworkService;
import com.example.demo.Service.SaleService;

//indica que es una clase controladora
@Controller
//define una ruta base para el controlador
@RequestMapping("")
public class ArtworkController {

//Autowired inyecta una dependencia para crear objetos
//se crean objetos para manejar los services de las entidades

    @Autowired
    private ArtworkService artworkService;
    @Autowired 
    private SaleService saleService;

    // Ruta donde se guardan las imágenes en static
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/assets/img/";

    // GET /obra → carga todas las obras
    @GetMapping
//model envia datos al frontend
    //requestparam obtiene los parametros enviados
    //required=false indica que es un parametro opcional
    public String index(Model model,@RequestParam(required = false) String message) {
        model.addAttribute("artwork", artworkService.findBySoldFalse()); //envia las obras a la vista, siempre que no estén vendidas
        model.addAttribute("sales", saleService.findAllSales()); //envía el historial de ventas a la vista
        model.addAttribute("message", message); //envía el mensaje opcional a la vista
        
        return "index";
    }

    // GET /obra/buscar → busca obras por nombre, artista o tipo
    //peticion get y ruta
    @GetMapping("/buscar")
    //requestparam obtiene los parametros enviados
    //define  que los rangos de precio son opcionales y sus valores por defecto son 0 para el mínimo y 99999999999999999 para el máximo
    //el valor maximo es lo suficientemente grande como para no limitar la busqueda
    public String search(@RequestParam String filter,  @RequestParam(required = false, defaultValue = "0") double min,
        @RequestParam(required = false, defaultValue = "99999999999999999") double max, Model model) {

       
        model.addAttribute("artwork", artworkService.findBySoldFalse()); //envia las obras a la vista, siempre que no estén vendidas
        try {
           model.addAttribute("results", artworkService.searchAvailable(filter, min, max)); //busca las obras con los filtros especificados y las muestra 
        } catch (IllegalArgumentException e) {
            return "redirect:/?message=" + e.getMessage(); //si no encuentra, redirige y muestra el mensaje de error
        }
        
        model.addAttribute("filter", filter); //envía el filtro especificado para verlo en la interfaz
        model.addAttribute("sales", saleService.findAllSales());  //envía el historial de ventas a la vista
        return "index";
    }

    // POST /obra/guardar → guarda una obra nueva
    //peticion post y ruta
    @PostMapping("/guardar")
    //requestparam obtiene los parametros enviados

    public String save(@RequestParam String name, @RequestParam String artist, @RequestParam double price, @RequestParam String type, @RequestParam MultipartFile image) throws IOException {
        
        //crea una instancia de la obra
        Artwork artwork = new Artwork();
        
        //asigna los valores recibidos desde el formulario
        artwork.setName(name);
        artwork.setArtist(artist);
        artwork.setPrice(price);
        artwork.setType(type);
        
        try{
            
            new File(UPLOAD_DIR).mkdirs(); //crea la carpeta donde la imagen se guarda en caso que no exista
            String fileName = image.getOriginalFilename(); //obtiene el nombre del nombre de archivo de la imagen
            image.transferTo(Paths.get(UPLOAD_DIR + fileName)); //guarda el archivo en la carpeta seleccionada
            artwork.setImage(fileName); //asocia el nombre de la imagen a la BD
            artworkService.saveArtwork(artwork); //guarda la obra

            return "redirect:/?message=saved"; //redirige y muestra un mensaje de confirmacion exitosa
        }catch(IllegalArgumentException e){
            return "redirect:/?message=" + e.getMessage(); //si falla, redirige y muestra un mensaje de error
        }
    }

    // POST /obra/eliminar → elimina una obra
    //peticion post y ruta
    @PostMapping("/eliminar")
    //requestparam obtiene los parametros enviados

    public String delete(@RequestParam int id) {
        
        try{
            artworkService.deleteArtwork(id); //borra la obra por la id asignada
            return "redirect:/?message=deleted"; //redirige y muestra un mensaje de confirmacion exitosa
        } catch(IllegalArgumentException e){
            return "redirect:/?message=" + e.getMessage(); //si falla, redirige y muestra un mensaje de error
        } catch(IllegalStateException a){
            return "redirect:/?message=" + a.getMessage(); //si falla, redirige y muestra un mensaje de error
        }
    }

    // POST /obra/actualizar → actualiza una obra
    @PostMapping("/actualizar")
    public String update(@RequestParam int id, @RequestParam String name, @RequestParam String artist, @RequestParam double price, @RequestParam String type, @RequestParam(required = false) MultipartFile image) throws IOException {

        //crea una instancia de la obra
        Artwork artwork = new Artwork();

        //asigna los valores recibidos desde el formulario
        artwork.setId(id);
        artwork.setName(name);
        artwork.setArtist(artist);
        artwork.setType(type);
        artwork.setPrice(price);

        //valida si la imagen esta vacia o no existe
        if (image != null && !image.isEmpty()) {
            
            new File(UPLOAD_DIR).mkdirs(); //crea la carpeta donde la imagen se guarda en caso que no exista
            String fileName = image.getOriginalFilename(); //obtiene el nombre del nombre de archivo de la imagen
            image.transferTo(Paths.get(UPLOAD_DIR + fileName)); //guarda el archivo en la carpeta seleccionada
            artwork.setImage(fileName); //asocia el nombre de la imagen a la BD
        }
        try {
            artworkService.updateArtwork(id, artwork); //actualiza la obra
            return "redirect:/?message=updated";  //redirige y muestra un mensaje de confirmacion exitosa
        } catch(IllegalArgumentException e){
            return "redirect:/?message=" + e.getMessage(); //si falla, redirige y muestra un mensaje de error
        } catch(IllegalStateException a){
            return "redirect:/?message=" + a.getMessage(); //si falla, redirige y muestra un mensaje de error
        }
        
    }
}