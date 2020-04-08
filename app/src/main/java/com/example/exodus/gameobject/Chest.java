package com.example.exodus.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.exodus.GameLoop;
import com.example.exodus.PVector;
import com.example.exodus.R;

public class Chest{
    private static float SPAWNS_PER_MINUTE = 2;
    private static float SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
    private static float UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
    private static float updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private int left, top, right, bottom;
    private int health = 3;

    private String gunName;
    private Context context;
    private PVector position;
    private Gun gun;
    private Drawable img;
    private Rect rect;

    public Chest(Context context, PVector position) {
        this.context = context;
        this.position = position;

        gun = GunContainer.getRandomGun();
        gunName = "R.drawable."+gun.getName()+".png";

        // Set rect size
        top = (int)position.y;
        left = (int)position.x;
        bottom = (int)position.y + 50;
        right = (int)position.x + 50;

        rect = new Rect(left, top, right, bottom);

        // Set image for chest and random weapon
        img = context.getResources().getDrawable(R.drawable.chest);

        // Set bounds for image
        img.setBounds(rect);
    }

    public void draw(Canvas canvas) {
        img.draw(canvas);
    }

    public static boolean readyToSpawn() {
        if(updatesUntilNextSpawn <= 0){
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }else{
            updatesUntilNextSpawn --;
            return false;
        }
    }

    public void open() {
        img = context.getResources().getDrawable(R.drawable.pistol);
        img.setBounds(rect);
    }

    public static boolean intersects(Circle obj, Chest chest) {
        boolean intersects = false;
        if(obj.getPositionX() + obj.radius >= chest.left &&
                obj.getPositionX() - obj.radius <= chest.right &&
                obj.getPositionY() - obj.radius <= chest.bottom &&
                obj.getPositionY() + obj.radius >= chest.top)
        {
            intersects = true;
        }
        return intersects;
    }

    public void subHealth() { health--; }

    public int getHealth() { return health; }
}
