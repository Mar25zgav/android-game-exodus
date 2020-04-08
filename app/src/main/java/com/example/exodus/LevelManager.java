package com.example.exodus;

import android.graphics.Canvas;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Chest;
import com.example.exodus.gameobject.Enemy;
import com.example.exodus.gameobject.Player;

import java.util.List;

public class LevelManager {
    private Player player;
    private Arena arena;
    private List<Enemy> enemyList;
    private List<Chest> chestList;

    private int killsTarget = 10;
    private float enemyRadius = 30;
    private static int enemyHealth = 2;

    public LevelManager(Player player, List<Enemy> enemyList, List<Chest> chestList, Arena arena) {
        this.player = player;
        this.arena = arena;
        this.enemyList = enemyList;
        this.chestList = chestList;
    }

    public void update() {
        // If player has enough kills -> open door
        if(player.getKills() >= killsTarget) {
            arena.openDoors();
        }

        // If player exits arena through door
        if(arena.leavesArena(player)) {
            // Set player position from doors
            arena.setPlayerPosition(player);

            // Add one health to player
            if(player.getHealth() < 5) {
                player.addHealth();
            }

            // Change arena color
            arena.changeColor();

            // Reset and power up enemies
            enemyList.clear();

            // Remove all chests
            chestList.clear();

            // Close arena doors
            arena.addDoors();

            killsTarget += 10;
            enemyHealth++;
        }
    }

    public void draw(Canvas canvas) {

    }

    public static int getEnemyHealth() { return enemyHealth; }

    public  float getEnemyRadius() { return enemyRadius; }
}
