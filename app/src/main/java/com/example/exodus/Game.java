package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Bullet;
import com.example.exodus.gameobject.Chest;
import com.example.exodus.gameobject.Circle;
import com.example.exodus.gameobject.Enemy;
import com.example.exodus.gameobject.GunContainer;
import com.example.exodus.gameobject.Player;
import com.example.exodus.gamepanel.GameOver;
import com.example.exodus.gamepanel.Hud;
import com.example.exodus.gamepanel.Inventory;
import com.example.exodus.gamepanel.Joystick;
import com.example.exodus.gamepanel.Performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Game class manages all objects in the game and is responsible for updating all states and render all objects to the screen
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private Arena arena;
    private Hud hud;
    private GameLoop gameLoop;
    private Player player;
    private Joystick joystick;
    private Performance performance;
    private GameOver gameOver;
    private LevelManager levelManager;
    private GunContainer gunContainer;
    private Inventory inventory;
    private PVector randomEnemyPos, randomChestPos;
    private Timer timer;
    private List<Bullet> bullets;
    private List<Chest> chestList;
    public static List<Enemy> enemyList;
    private int joystickPointerId = 0;
    private boolean shooting = false;

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        // Initialize game panels
        joystick = new Joystick();
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);

        // Initialize game objects
        player = new Player(getContext(), joystick);
        arena = new Arena(getContext());
        timer = new Timer();
        hud = new Hud(getContext(), timer);
        gunContainer = new GunContainer(getContext());
        enemyList = new ArrayList<>();
        bullets = new ArrayList<>();
        chestList = new ArrayList<>();
        inventory = new Inventory(context, player);
        levelManager = new LevelManager(player, enemyList, chestList, arena);
        timer.start();

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Handle touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed before this event -> cast spell
                    shooting = true;
                } else if (joystick.isPressed(event.getX(), event.getY())) {
                    // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    // Joystick was not previously, and is not pressed in this event -> cast spell
                    shooting = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator(event.getX(), event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // joystick pointer was let go off
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                shooting = false;
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        arena.draw(canvas);

        joystick.draw(canvas);

        for (Chest chest : chestList) {
            chest.draw(canvas);
        }

        player.draw(canvas);

        for (Enemy enemy : enemyList) {
            enemy.draw(canvas);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }

        // Draw game panels
        hud.draw(canvas);
        performance.draw(canvas);
        inventory.draw(canvas);

        // Draw Game over if the player is dead
        if (Player.getHealth() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {
        // Stop updating the game if the player is dead
        if (Player.getHealth() <= 0) {
            gameLoop.stopLoop();
        }

        // Update game state
        joystick.update();
        player.update();

        // Spawn enemy if it is time, but not on top of each other
        if (Enemy.readyToSpawn()) {
            randomEnemyPos = PVector.getRandomEnemyPos(player, enemyList);
            enemyList.add(new Enemy(getContext(), player, randomEnemyPos.x, randomEnemyPos.y, LevelManager.getEnemyRadius()));
        }

        // Update state of each enemy
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        // Add spell if it is time
        if (shooting && Bullet.readyToShoot()) {
            bullets.add(new Bullet(getContext(), player));
        }

        // Update spell pozitions
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        // Spawn chest if it is time
        if (Chest.readyToSpawn()) {
            randomChestPos = PVector.getRandomChestPos(player, enemyList, chestList);
            chestList.add(new Chest(getContext(), randomChestPos));
        }

        // Iterate through enemyList and check for collision between each enemy and the player
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        Enemy enemy;
        while (iteratorEnemy.hasNext()) {
            enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                // Remove enemy if colliding with player
                iteratorEnemy.remove();
                player.subHealth();
                continue;
            }

            Iterator<Bullet> iteratorBullet = bullets.iterator();
            while (iteratorBullet.hasNext()) {
                Circle bullet = iteratorBullet.next();
                // Check if enemy collides with spell
                if (Circle.isColliding(bullet, enemy)) {
                    // Hit enemy with bullet
                    enemy.subHealth(Bullet.getDamage());
                    // Remove enemy if lost all lives else subtract health
                    if (enemy.getHealth() <= 0) {
                        iteratorBullet.remove();
                        iteratorEnemy.remove();
                        player.addKill();
                    } else {
                        iteratorBullet.remove();
                    }
                }
            }
        }

        // Iterate through spellList and check for collision between spell and arena
        Iterator<Bullet> iteratorBullet = bullets.iterator();
        while (iteratorBullet.hasNext()) {
            Circle bullet = iteratorBullet.next();
            // If spell hit arena
            if (Arena.collision(bullet)) {
                iteratorBullet.remove();
            }
            // If there is a chest, check for collision
            if (chestList.size() != 0) {
                Iterator<Chest> chestIterator = chestList.iterator();
                while (chestIterator.hasNext()) {
                    Chest chest = chestIterator.next();
                    // If bullet hits chest open
                    if (Chest.intersects(bullet, chest) && !chest.isOpen()) {
                        chest.subHealth(Bullet.getDamage());
                        iteratorBullet.remove();
                    }
                    // If chest out of lives -> open it
                    if (chest.getHealth() <= 0 && !chest.isOpen()) {
                        chest.open();
                    }
                }
            }
        }

        // Iterate through chests and check for collision between player and chest
        Iterator<Chest> chestIterator = chestList.iterator();
        while (chestIterator.hasNext()) {
            Chest chest = chestIterator.next();
            // Check if chest open
            if (chest.isOpen()) {
                // If player touches open chest -> equip weapon
                if (Chest.intersects(player, chest)) {
                    inventory.addItem(chest.getGun());
                    chestIterator.remove();
                }
            }
        }

        // Update level
        levelManager.update();
    }

    public void pause() {
        timer.pause();
        gameLoop.stopLoop();
    }

    public void resume() {
        timer.resume();
        gameLoop.startLoop();
    }
}
