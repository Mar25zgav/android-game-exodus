package com.example.exodus.gameobject;

public class Gun {
    private String name;
    private int force;
    private float speed;
    private float radius;
    private double damage;
    private double fireRate;

    public Gun(String name, float speed, double damage, float radius, int force, double fireRate) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.radius = radius;
        this.force = force;
        this.fireRate = fireRate;
    }

    public String getName() {
        return name;
    }

    public float getSpeed() {
        return speed;
    }

    public double getDamage() {
        return damage;
    }

    public float getRadius() {
        return radius;
    }

    public int getForce() {
        return force;
    }

    public double getFireRate() {
        return fireRate;
    }
}
