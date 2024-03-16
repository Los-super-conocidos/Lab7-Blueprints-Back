/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author hcadavid
 */
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {
    private final ConcurrentMap<Tuple<String,String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts = new Point[]{new Point(140, 140),new Point(115, 115)};
        Point[] pts2 = new Point[]{new Point(14, 140),new Point(15, 115)};
        Point[] pts3 = new Point[]{new Point(140, 14),new Point(115, 15)};
        Point[] pts4 = new Point[]{new Point(14, 200),new Point(500, 115)};
        Point[] pts5 = new Point[]{new Point(50, 50),new Point(100, 100),new Point(150, 50),new Point(200, 100),new Point(250, 50)};
        Point[] pts6 = new Point[]{new Point(100, 200),new Point(300, 200),new Point(200, 100),new Point(100, 200),new Point(300, 200),new Point(300,350),new Point(100, 350),new Point(100, 200)};

        Blueprint bp = new Blueprint("", "Blue_one",pts);
        Blueprint bp2 = new Blueprint("Christian", "Print_one",pts2);
        Blueprint bp3 = new Blueprint("Sebastian", "Blue_two",pts3);
        Blueprint bp4 = new Blueprint("Christian", "Print_two",pts4);
        Blueprint bp5 = new Blueprint("Carolina", "medina",pts5);
        Blueprint bp6 = new Blueprint("Carolina", "casa",pts6);

        //Blueprint bp5 = new Blueprint("juan", "Blue_one");

        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(),bp3.getName()), bp3);
        blueprints.put(new Tuple<>(bp4.getAuthor(),bp4.getName()), bp4);
        blueprints.put(new Tuple<>(bp5.getAuthor(),bp5.getName()), bp5);
        blueprints.put(new Tuple<>(bp6.getAuthor(),bp6.getName()), bp6);

    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.putIfAbsent(new Tuple<>(bp.getAuthor(),bp.getName()), bp) != null) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        Blueprint blueprint = blueprints.get(new Tuple<>(author, bprintname));
        if (blueprint == null) {
            throw new BlueprintNotFoundException("Blueprint not found: " + author + " - " + bprintname);
        }
        return blueprint;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = ConcurrentHashMap.newKeySet();
        for (Blueprint blueprint : blueprints.values()) {
            if(blueprint.getAuthor() != null) {
                if (blueprint.getAuthor().equals(author)) {
                    authorBlueprints.add(blueprint);
                }
            }
        }
        if (authorBlueprints.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints found for author: " + author);
        }
        return authorBlueprints;
    }

    @Override
    public ConcurrentMap<Tuple<String,String>, Blueprint> getAllBlueprints(){
        return blueprints;
    }

    @Override
    public void updateBluePrint(String author, String bpname, Blueprint bpUpdate) throws BlueprintNotFoundException {
        Blueprint bpActual = getBlueprint(author, bpname);
        bpActual.setPoints(bpUpdate.getPoints());
    }

}
