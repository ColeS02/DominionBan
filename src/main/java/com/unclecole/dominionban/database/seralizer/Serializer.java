package com.unclecole.dominionban.database.seralizer;

import com.unclecole.dominionban.DominionBan;

public class Serializer {


    /**
     * Saves your class to a .json file.
     */
    public void save(Object instance) {
        DominionBan.getPersist().save(instance);
    }

    /**
     * Loads your class from a json file
     *
   */
    public <T> T load(T def, Class<T> clazz, String name) {
        return DominionBan.getPersist().loadOrSaveDefault(def, clazz, name);
    }



}
