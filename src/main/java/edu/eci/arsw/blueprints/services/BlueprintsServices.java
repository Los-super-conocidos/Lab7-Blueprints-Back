/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BluePrintsFiltred;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import java.util.*;

import edu.eci.arsw.blueprints.persistence.impl.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {
    @Autowired
    BlueprintsPersistence bpp;
    @Autowired
    BluePrintsFiltred filtred;

    @Autowired
    public BlueprintsServices(BlueprintsPersistence blueprintsPersistence,BluePrintsFiltred bluePrintsFiltred){
        this.bpp=blueprintsPersistence;
        this.filtred=bluePrintsFiltred;
    }
    
    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        bpp.saveBlueprint(bp);
    }
    
    public Map<Tuple<String,String>,Blueprint> getAllBlueprints() throws BlueprintPersistenceException {
        return bpp.getAllBlueprints();
    }
    
    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */

    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return bpp.getBlueprint(author, name);
    }
    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        return bpp.getBlueprintsByAuthor(author);
    }

    /**
     *
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author but filtered.
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getFilteredBlueprint(String author, String name) throws BlueprintNotFoundException {
        return filtred.getFlat(bpp.getBlueprint(author,name));
    }

    public void updateBluePrint(String author, String bpname, Blueprint bp) throws BlueprintNotFoundException {
        Blueprint blueprint = getBlueprint(author,bpname);
        bpp.updateBluePrint(author,bpname,bp);
    }

    public void deleteBlueprint(String author, String bpname)throws BlueprintNotFoundException{
        Blueprint blueprint = getBlueprint(author,bpname);
        if(blueprint == null){
            throw new BlueprintNotFoundException("Blueprint doesn´t exist");
        }else {
            bpp.deleteBLueprint(author,bpname,blueprint);
        }
    }
}
