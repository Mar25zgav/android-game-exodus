package com.example.exodus.gameobject;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.exodus.GameLoop;
import com.example.exodus.R;

public class Gun {
    private float speed = 800; // Pixels per second
    private float max_speed, spread;
    private int damage;
    private String name;

    public Gun(String name, float speed, int damage) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;

        max_speed = speed / GameLoop.MAX_UPS;
    }

    public String getName() { return name; }

    public float getSpeed() { return max_speed; }

    public int getDamage() { return damage; }
}
