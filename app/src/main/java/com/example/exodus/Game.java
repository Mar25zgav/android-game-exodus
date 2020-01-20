package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.core.content.ContextCompat;

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

    private int joystickPointerId = 0;
    private int numberOfSpellsToCast = 0;
    public static int enemiesKilled;
    public static List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        //Inicializacija igralca, joystika, arene, huda
        joystick = new Joystick(300, 800, 120, 75);
        player = new Player(getContext(), joystick, GameActivity.getScreenWidth()/2, GameActivity.getScreenHeight()/2, 35);
        arena = new Arena(getContext(), player);
        hud = new Hud(getContext());
        enemiesKilled = 0;

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
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
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
                    joystick.setActuator((double) event.getX(), (double) event.getY());
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
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background));
        //drawUPS(canvas);
        drawFPS(canvas);
        joystick.draw(canvas);
        arena.draw(canvas);
        player.draw(canvas);

        for (Enemy enemy : enemyList) {
            enemy.draw(canvas);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas);
        }

        hud.draw(canvas);
    }

    public void update() {
        //Update game state
        joystick.update();
        arena.update();
        player.update();
        hud.update();

        if(Player.health<=0){
            Player.health = 5;
            Hud.timer.stop();
            Hud.timer.start();
        }

        // Spawn enemy if it is time
        if (Enemy.readyToSpawn()){
            enemyList.add(new Enemy(getContext(), player));
        }

        // Update state of each enemy
        for (Enemy enemy : enemyList){
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
        while (iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if(Circle.isColliding(enemy, player)){
                // Remove enemy if colliding with player
                iteratorEnemy.remove();
                player.health -= 1;
                continue;
            }

            Iterator<Spell> iteratorSpell = spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();
                // Remove enemy if it collides with a spell
                if (Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    enemiesKilled++;
                }
            }
        }

        // Iterate through spellList and check for collision between spell and arena
        Iterator<Spell> iteratorSpell = spellList.iterator();
        while (iteratorSpell.hasNext()) {
            Circle spell = iteratorSpell.next();
            if (Arena.collision(spell)){
                iteratorSpell.remove();
            }
        }
    }

    public void drawFPS(Canvas canvas) {
        String avarageFPS = Double.toString((int) gameLoop.getAvarageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + avarageFPS, 100, 150, paint);
    }

    public void drawUPS(Canvas canvas) {
        String avarageUPS = Double.toString(gameLoop.getAvarageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + avarageUPS, 100, 100, paint);
    }
}
