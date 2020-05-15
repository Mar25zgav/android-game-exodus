package com.example.exodus.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.exodus.LevelManager;
import com.example.exodus.Timer;
import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Bullet;
import com.example.exodus.gameobject.Gun;
import com.example.exodus.gameobject.Player;
import com.example.exodus.menupanel.GameActivity;

public class Inventory {
    private Rect rect;
    private Drawable gunImage;
    private Timer timer;
    private Player player;
    private Context context;
    private int top, left, bottom, right;
    private float wallSize = Arena.getWallSize();
    private float timerWidth = Hud.getTimerWidth();
    private float timerHeight = Hud.getTimerHeight();
    private float screenWidth = GameActivity.getScreenWidth();
    private int padding = (int) (GameActivity.getScreenHeight() * 0.005);

    public Inventory(Context context, Player player) {
        this.player = player;
        this.context = context;

        timer = new Timer();

        // Set rect size for gun
        top = (int) (wallSize * 1.45) + padding;
        left = (int) (screenWidth / 2 + timerWidth * 2) + padding;
        bottom = (int) (wallSize + timerHeight * 1.1);
        right = left + (bottom - top);

        rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        // If player has picked up a weapon
        if (player.hasGun()) {
            // Player can use weapon only for 10s
            if (timer.getElapsedTime() <= LevelManager.getWeaponTime()) {
                gunImage.draw(canvas);
            } else {
                timer.cancel();
                player.takeAwayGun();
                Bullet.reset();
            }
        }
    }

    public void addItem(Gun gun) {
        // Set gun image to draw later
        int id = context.getResources().getIdentifier(gun.getName(), "drawable", context.getPackageName());
        gunImage = context.getResources().getDrawable(id);
        gunImage.setBounds(rect);

        // Start timer for weapon
        timer.cancel();
        timer.start();

        // Give player the gun
        player.setGun(gun);

        // Set bullet to match gun properties
        Bullet.setSpeed(gun.getSpeed());
        Bullet.setRadius(gun.getRadius());
        Bullet.setDamage(gun.getDamage());
        Bullet.setBps(gun.getFireRate());
    }
}
