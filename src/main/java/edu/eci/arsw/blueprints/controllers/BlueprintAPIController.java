/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.impl.Tuple;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/blueprints")
/**
 *
 * @author hcadavid
 */
public class BlueprintAPIController {

    @Autowired
    private BlueprintsServices blueprintsServices;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetAllBluePrints(){
        try {
            Map<Tuple<String, String>, Blueprint> listBluePrints = blueprintsServices.getAllBlueprints();
            return new ResponseEntity<>(listBluePrints,HttpStatus.ACCEPTED);
        } catch (Exception ex){
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al obtener los planos: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{author}")
    public ResponseEntity<?> manejadorGetBluePrintsByAuthor(@PathVariable("author") String author){
        try{
            return new ResponseEntity<>(blueprintsServices.getBlueprintsByAuthor(author),HttpStatus.ACCEPTED);
        } catch (Exception ex){
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping(path = "/{author}/{bpname}")
    public ResponseEntity<?> manejadorGetBluePrint(@PathVariable("author") String author,  @PathVariable("bpname") String bpname){
        try{
            return new ResponseEntity<>(blueprintsServices.getBlueprint(author,bpname),HttpStatus.ACCEPTED);
        } catch (Exception ex){
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPostBluePrint(@RequestBody Blueprint bp){
        try{
            blueprintsServices.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception ex){
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> manejadorPutBluePrint(@RequestBody Map<String, Object> payload) {
        try {
            String author = (String) payload.get("author");
            String bpname = (String) payload.get("name");
            List<Map<String, Integer>> points = (List<Map<String, Integer>>) payload.get("points");

            Point[] pointArray = new Point[points.size()];
            for (int i = 0; i < points.size(); i++) {
                Map<String, Integer> point = points.get(i);
                int x = point.get("x");
                int y = point.get("y");
                pointArray[i] = new Point(x, y);
            }

            Blueprint bp = new Blueprint(bpname, author,pointArray);

            blueprintsServices.updateBluePrint(author, bpname,bp);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception ex) {
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(path ="/{author}/{bpname}")
    public ResponseEntity<?> manejadorPutBluePrint(@PathVariable("author") String author,  @PathVariable("bpname") String bpname,@RequestBody Map<String, Object> payload){
        try{
            List<Map<String, Integer>> points = (List<Map<String, Integer>>) payload.get("points");
            Point[] pointArray = new Point[points.size()];
            for (int i = 0; i < points.size(); i++) {
                Map<String, Integer> point = points.get(i);
                int x = point.get("x");
                int y = point.get("y");
                pointArray[i] = new Point(x, y);
            }

            Blueprint bp = new Blueprint(bpname, author,pointArray);
            blueprintsServices.updateBluePrint(author,bpname,bp);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception ex){
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(path ="/{author}/{bpname}")
    public ResponseEntity<?> deleteBlueprint(@PathVariable("author") String author,  @PathVariable("bpname") String bpname){
        try{
            blueprintsServices.deleteBlueprint(author, bpname);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(Blueprint.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}

