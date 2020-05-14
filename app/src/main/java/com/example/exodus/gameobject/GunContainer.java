package com.example.exodus.gameobject;

import android.content.Context;

import com.example.exodus.menupanel.MainActivity;

import java.util.ArrayList;

public class GunContainer {
    private static Gun pistol, musket, uzi, rifle, sniper, machine;
    private static ArrayList<Gun> guns = new ArrayList<>();
    private static ArrayList<Gun> availableGuns = new ArrayList<>();
    private float spellRadius = Bullet.getStaticRadius();

    public GunContainer(Context context) {
        pistol = new Gun("pistol", 1400, 2, spellRadius * 0.90f, 50, 2.5);
        uzi = new Gun("uzi", 1800, 0.8, spellRadius * 0.65f, 30, 8);
        musket = new Gun("musket", 1000, 3, spellRadius * 0.95f, 80, 1.5);
        rifle = new Gun("rifle", 1600, 3, spellRadius, 40, 6);
        sniper = new Gun("sniper", 2000, 30, spellRadius * 1.1f, 100, 1);
        machine = new Gun("machine", 1800, 2, spellRadius, 50, 10);

        guns.add(pistol);
        guns.add(uzi);
        guns.add(musket);
        guns.add(rifle);
        guns.add(sniper);
        guns.add(machine);

        int weaponsUnlocked = MainActivity.getInstance().getWeaponsNum();

        int i = 0;
        while (i < weaponsUnlocked) {
            availableGuns.add(guns.get(i));
            i++;
        }

    }

    static Gun getRandomGun() {
        int x = (int) (Math.random() * availableGuns.size());
        return availableGuns.get(x);
    }
}
