package com.unclecole.dominionban.database;

import com.unclecole.dominionban.database.seralizer.Serializer;
import com.unclecole.dominionban.objects.BanObject;

import java.util.HashMap;
import java.util.UUID;

public class BanData {

    public static transient BanData instance = new BanData();
    public static transient HashMap<UUID, BanObject> bannedPlayers = new HashMap<>();

    public static void save() {
        new Serializer().save(instance);
    }

    public static void load() {
        new Serializer().load(instance, BanData.class, "bandata");
    }

}
