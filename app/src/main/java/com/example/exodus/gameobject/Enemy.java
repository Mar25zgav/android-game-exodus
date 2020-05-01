package com.example.exodus.gameobject;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.exodus.GameLoop;
import com.example.exodus.LevelManager;
import com.example.exodus.PVector;
import com.example.exodus.R;

/* Enemy is a character which always moves in the direction of the player.
   The enemy class is an extension of a circle, which is an extension of a GameObject */
public class Enemy extends Circle {
    private Player player;
    private Context context;
    private PVector distanceToPlayerV;
    private double timer;
    private boolean hit = false;
    private static double health;
    private static float SPEED_PIXELS_PER_SECOND = 130;
    private static float MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static float SPAWNS_PER_MINUTE = 20;
    private static float SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
    private static float UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
    private static float updatesUntilNextSpawn = UPDATES_PER_SPAWN;

    public Enemy(Context context, Player player, float positionX, float positionY, float radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
        this.context = context;

        timer = System.currentTimeMillis();
        health = LevelManager.getEnemyHealth();
    }

    // Preveri ali je že čas za novega glede na nastavljeno število spawnov na minuto
    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn--;
            return false;
        }
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - timer > 500) {
            // Update velocity of the player, so that the velocity is in the direction of the player.
            // Calculate vector from enemy to player (in x and y).
            distanceToPlayerV = new PVector(player.getPositionX() - position.x, player.getPositionY() - position.y);

            // Calculate (absolute) distance between enemy and player
            float distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

            // Calculate direction from enemy to player.
            direction.set(distanceToPlayerV.div(distanceToPlayer));

            // Set velocity in the direction to the player.
            if (distanceToPlayer > 0) // Avoid division by 0
                velocity.set(direction.mult(MAX_SPEED));
            else
                velocity.set(0, 0);

            // If enemy hit then knockback with weapon force
            if (hit && player.hasGun()) {
                velocity.normalize().mult(-player.getGun().getForce());
                hit = false;
            }

            // Save current position, if collides set position to previous
            position.x += velocity.x;
            if (Arena.collision(this)) {
                position.x -= velocity.x;
            }
            position.y += velocity.y;
            if (Arena.collision(this)) {
                position.y -= velocity.y;
            }
        }
    }


    public static double getHealth() {
        return health;
    }

    public static void addSpeed(float speed) {
        SPEED_PIXELS_PER_SECOND += speed;
        MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    }

    public static void addSpawns(float spawns) {
        SPAWNS_PER_MINUTE += spawns;
        SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
        UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
        updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    }

    public void subHealth(double damage) {
        health -= damage;
        hit = true;
    }

    public void reset() {
        SPEED_PIXELS_PER_SECOND = 130;
        MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
        SPAWNS_PER_MINUTE = 20;
        SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
        UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
        updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    }
}
