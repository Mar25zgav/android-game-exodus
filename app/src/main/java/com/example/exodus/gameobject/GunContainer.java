package com.example.exodus.gameobject;

import java.util.ArrayList;

public class GunContainer {
    private static Gun pistol, musket, uzi, rifle, sniper, machine;
    private static ArrayList<Gun> guns = new ArrayList<Gun>();

    public GunContainer() {
        pistol = new Gun("pistol", 800,1);
        musket = new Gun("musket", 600,2);
        uzi = new Gun("uzi", 1600, 1);
        rifle = new Gun("rifle", 1300, 3);
        sniper = new Gun("sniper", 2000, 5);
        machine = new Gun("machine", 1300, 3);

        guns.add(pistol);
        guns.add(musket);
        guns.add(uzi);
        guns.add(rifle);
        guns.add(sniper);
        guns.add(machine);
    }

    public static Gun getGun(int x) {
        return guns.get(x);
    }

    public static Gun getRandomGun() {
        int x = (int) (Math.random()* guns.size());
        return guns.get(x);
    }
}
