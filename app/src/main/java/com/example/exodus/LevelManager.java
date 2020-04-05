package com.example.exodus;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Enemy;
import com.example.exodus.gameobject.Player;

import java.util.List;

public class LevelManager {
    private Player player;
    private Arena arena;
    private List<Enemy> enemyList;

    private int killsTarget;

    public LevelManager(Player player, List<Enemy> enemyList, Arena arena) {
        this.player = player;
        this.arena = arena;
        this.enemyList = enemyList;

        killsTarget = 10;
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

            killsTarget += 10;

            // Change arena color
            arena.changeColor();

            // Reset and power up enemies
            enemyList.clear();

            // Close arena doors
            arena.addDoors();
        }
    }
}
