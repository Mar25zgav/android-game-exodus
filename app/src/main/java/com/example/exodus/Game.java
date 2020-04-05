package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.core.content.ContextCompat;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Circle;
import com.example.exodus.gameobject.Enemy;
import com.example.exodus.gameobject.Player;
import com.example.exodus.gameobject.Spell;
import com.example.exodus.gamepanel.GameOver;
import com.example.exodus.gamepanel.Hud;
import com.example.exodus.gamepanel.Joystick;
import com.example.exodus.gamepanel.Performance;
import com.example.exodus.menupanel.GameActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Game class manages all objects in the game and is responsible for updating all states and render all objects to the screen
public class Game extends SurfaceView implements SurfaceHolder.Callback{
    private Arena arena;
    private Hud hud;
    private GameLoop gameLoop;
    private Player player;
    private Joystick joystick;
    private Performance performance;
    private GameOver gameOver;
    private LevelManager levelManager;
    private PVector randomEnemyPos;

    public static List<Enemy> enemyList;
    private List<Spell> spellList;
    private int joystickPointerId = 0;
    private int numberOfSpellsToCast = 0;
    private float enemyRadius = 30;

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        // Initialize game panels
        joystick = new Joystick(300, 800, 120, 75);
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);

        // Initialize game objects
        player = new Player(getContext(), joystick, GameActivity.getScreenWidth()/2, GameActivity.getScreenHeight()/2, 35);
        arena = new Arena(getContext());
        hud = new Hud(getContext());
        enemyList = new ArrayList<>();
        spellList = new ArrayList<>();

        levelManager = new LevelManager(player, enemyList, arena);

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
                    numberOfSpellsToCast++;
                } else if (joystick.isPressed(event.getX(), event.getY())) {
                    // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    // Joystick was not previously, and is not pressed in this event -> cast spell
                    numberOfSpellsToCast++;
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

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background));

        // Draw game objects
        joystick.draw(canvas);
        arena.draw(canvas);
        player.draw(canvas);

        for (Enemy enemy : enemyList) {
            enemy.draw(canvas);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas);
        }

        // Draw game panels
        //performance.draw(canvas);
        hud.draw(canvas);

        // Draw Game over if the player is dead
        if (player.getHealth() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {
        // Stop updating the game if the player is dead
        if(player.getHealth() <= 0) {
            gameLoop.stopLoop();
        }

        // Update object state
        joystick.update();
        player.update();

        // Spawn enemy if it is time, but not on top of each other
        if(Enemy.readyToSpawn()) {
            randomEnemyPos = PVector.getRandomPos(player, enemyList);
            enemyList.add(new Enemy(getContext(), player, randomEnemyPos.x, randomEnemyPos.y, enemyRadius));
        }

        // Update state of each enemy
        for(Enemy enemy : enemyList) {
            enemy.update();
        }

        // Update states of all spells
        while (numberOfSpellsToCast > 0) {
            spellList.add(new Spell(getContext(), player));
            numberOfSpellsToCast--;
        }
        for (Spell spell : spellList) {
            spell.update();
        }

        // Iterate through enemyList and check for collision between each enemy and the player
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while(iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if(Circle.isColliding(enemy, player)){
                // Remove enemy if colliding with player
                iteratorEnemy.remove();
                player.subHealth();
                continue;
            }

            Iterator<Spell> iteratorSpell = spellList.iterator();
            while(iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();
                // Remove enemy if it collides with a spell
                if (Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    player.addKill();
                }
            }
        }

        // Iterate through spellList and check for collision between spell and arena
        Iterator<Spell> iteratorSpell = spellList.iterator();
        while(iteratorSpell.hasNext()) {
            Circle spell = iteratorSpell.next();
            if (Arena.collision(spell)) {
                iteratorSpell.remove();
            }
        }

        // Update level
        levelManager.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
