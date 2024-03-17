/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.impl.Tuple;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author hcadavid
 */
public interface BlueprintsPersistence {

    /**
     *
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists,
     *    or any other low-level persistence error occurs.
     */
     void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    /**
     *
     * @param author blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
     Blueprint getBlueprint(String author,String bprintname) throws BlueprintNotFoundException;


    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;

    Map<Tuple<String,String>,Blueprint> getAllBlueprints() throws  BlueprintPersistenceException;

    void updateBluePrint(String author, String bpname, Blueprint bpUpdate) throws BlueprintNotFoundException;

    Set<Blueprint> deleteBLueprint(String author,String bpname, Blueprint blueprint) throws BlueprintNotFoundException;
}
