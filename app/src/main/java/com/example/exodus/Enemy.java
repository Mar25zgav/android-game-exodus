package com.example.exodus;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.example.exodus.Activities.GameActivity;
import com.example.exodus.Activities.MainActivity;

/* Enemy is a character which always moves in the direction of the player.
   The enemy class is an extension of a circle, which is an extension of a GameObject */
public class Enemy extends Circle{
    private static double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.4;
    private static double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private static Player player;
    private double timer;
    public static int health = 1;

    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
        this.positionX = positionX;
        this.positionY = positionY;
        timer = System.currentTimeMillis();
    }

    public Enemy(Context context, Player player) {
        super(context,
                ContextCompat.getColor(context, R.color.enemy),
                randPositionX() ,
                randPositionY(),
                30
        );
        this.player = player;
        timer = System.currentTimeMillis();
    }

    public static double randPositionX(){
        return Math.random()*((GameActivity.width-Arena.wallSize-30)-(Arena.wallSize+30))+(Arena.wallSize+30);
    }

    public static double randPositionY(){
        return Math.random()*((GameActivity.height-Arena.wallSize-30)-(Arena.wallSize+30))+(Arena.wallSize+30);
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
        if(System.currentTimeMillis()-timer > 500){
            // Update velocity of the player, so that the velocity is in the direction of the player.
            // Calculate vector from enemy to player (in x and y).
            double distanceToPlayerX = player.getPositionX() - positionX;
            double distanceToPlayerY = player.getPositionY() - positionY;

            // Calculate (absolute) distance between enemy and player
            double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

            // Calculate direction from enemy to player.
            double directionX = distanceToPlayerX / distanceToPlayer;
            double directionY = distanceToPlayerY / distanceToPlayer;

            // Set velocity in the direction to the player.
            if(distanceToPlayer > 0){ // Avoid division by 0
                velocityX = directionX * MAX_SPEED;
                velocityY = directionY * MAX_SPEED;
            }else{
                velocityX = 0;
                velocityY = 0;
            }

            positionX += velocityX;
            positionY += velocityY;

            // Every time we update an enemy increase speed
            MAX_SPEED += 0;
            SPAWNS_PER_MINUTE += 10;
        }
    }
}
