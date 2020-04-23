package com.example.exodus.gameobject;

import android.content.Context;
import java.util.ArrayList;

public class GunContainer {
    private static Gun pistol, musket, uzi, rifle, sniper, machine;
    private static ArrayList<Gun> guns = new ArrayList<>();

    public GunContainer(Context context) {
        pistol = new Gun("pistol", 900,2, 8, 10);
        musket = new Gun("musket", 600,2, 15, 20);
        uzi = new Gun("uzi", 1600, 1, 10, 5);
        rifle = new Gun("rifle", 1300, 3,16, 30);
        sniper = new Gun("sniper", 2000, 5, 18, 50);
        machine = new Gun("machine", 1300, 3,18, 40);

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

    static Gun getRandomGun() {
        int x = (int) (Math.random()* guns.size());
        return guns.get(x);
    }
}
