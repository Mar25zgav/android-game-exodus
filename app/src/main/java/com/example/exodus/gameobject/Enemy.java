package com.example.exodus.gameobject;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.example.exodus.GameLoop;
import com.example.exodus.PVector;
import com.example.exodus.R;
import com.example.exodus.gameobject.Circle;
import com.example.exodus.gameobject.GameObject;
import com.example.exodus.gameobject.Player;

/* Enemy is a character which always moves in the direction of the player.
   The enemy class is an extension of a circle, which is an extension of a GameObject */
public class Enemy extends Circle {
    private static float SPEED_PIXELS_PER_SECOND = (float)(Player.SPEED_PIXELS_PER_SECOND * 0.55);
    private static float MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static float SPAWNS_PER_MINUTE = 30;
    private static float SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
    private static float UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
    private static float updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private Player player;
    private double timer;
    private PVector distanceToPlayerV;

    public Enemy(Context context, Player player, float positionX, float positionY, float radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
        timer = System.currentTimeMillis();
    }

    // Preveri ali je že čas za novega glede na nastavljeno število spawnov na minuto
    public static boolean readyToSpawn() {
        if(updatesUntilNextSpawn <= 0){
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }else{
            updatesUntilNextSpawn --;
            return false;
        }
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() - timer > 500){
            // Update velocity of the player, so that the velocity is in the direction of the player.
            // Calculate vector from enemy to player (in x and y).
            distanceToPlayerV = new PVector(player.getPositionX() - position.x, player.getPositionY() - position.y);

            // Calculate (absolute) distance between enemy and player
            float distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

            // Calculate direction from enemy to player.
            direction.set(distanceToPlayerV.div(distanceToPlayer));

            // Set velocity in the direction to the player.
            if(distanceToPlayer > 0) // Avoid division by 0
                velocity.set(direction.mult(MAX_SPEED));
            else
                velocity.set(0,0);
            position.add(velocity);
        }
    }
}
