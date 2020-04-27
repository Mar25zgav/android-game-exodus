package com.example.exodus.gameobject;

import android.content.Context;
import java.util.ArrayList;

public class GunContainer {
    private static Gun pistol, musket, uzi, rifle, sniper, machine;
    private static ArrayList<Gun> guns = new ArrayList<>();
    private float spellRadius = Bullet.getStaticRadius();

    public GunContainer(Context context) {
        pistol = new Gun("pistol", 1400, 2, spellRadius * 0.90f, 50, 2.5);
        uzi = new Gun("uzi", 1800, 0.2, spellRadius * 0.65f, 30, 8);
        musket = new Gun("musket", 1400, 3, spellRadius * 0.95f, 80, 1.5);
        rifle = new Gun("rifle", 1600, 3, spellRadius, 40, 6);
        sniper = new Gun("sniper", 2000, 30, spellRadius * 1.1f, 100, 1);
        machine = new Gun("machine", 1800, 2, spellRadius, 50, 10);

        guns.add(pistol);
        guns.add(uzi);
        guns.add(musket);
        guns.add(rifle);
        guns.add(sniper);
        guns.add(machine);
    }

    public static Gun getGun(int x) {
        return guns.get(x);
    }

    static Gun getRandomGun() {
        int x = (int) (Math.random() * guns.size());
        return guns.get(x);
    }
}
