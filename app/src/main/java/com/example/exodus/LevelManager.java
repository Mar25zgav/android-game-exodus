package com.example.exodus;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Chest;
import com.example.exodus.gameobject.Enemy;
import com.example.exodus.gameobject.Player;
import com.example.exodus.menupanel.GameActivity;

import java.util.List;

public class LevelManager {
    private Player player;
    private Arena arena;
    private List<Enemy> enemyList;
    private List<Chest> chestList;
    private static float gameHeight = GameActivity.getScreenHeight();
    private static float gameWidth = GameActivity.getScreenWidth();
    private static float enemyRadius = gameHeight / 37;
    private static double enemyHealth = 1;
    private int killsTarget = 10;

    public LevelManager(Player player, List<Enemy> enemyList, List<Chest> chestList, Arena arena) {
        this.player = player;
        this.arena = arena;
        this.enemyList = enemyList;
        this.chestList = chestList;
    }

    public void update() {
        // If player has enough kills -> open door
        if (player.getKills() >= killsTarget) {
            arena.openDoors();
        }

        // If player exits arena through door
        if (arena.leavesArena(player)) {
            // Set player position from doors
            arena.setPlayerPosition(player);

            // Reset player kills
            player.resetKills();

            // Add speed to player
            Player.addSpeed(8);

            // Add one health to player
            if (player.getHealth() < 5) {
                player.addHealth();
            }

            // Change arena color
            arena.changeColor();

            // Reset and power up enemies
            enemyList.clear();
            enemyHealth++;
            Enemy.addSpeed(10);
            Enemy.addSpawns(4);

            // Remove all chests add spawns per minute
            chestList.clear();
            Chest.addSpawns(2);

            // Close arena doors
            arena.addDoors();

            // Increment kills target
            killsTarget += 2;
        }
    }

    public static double getEnemyHealth() {
        return enemyHealth;
    }

    public static float getEnemyRadius() {
        return enemyRadius;
    }
}
