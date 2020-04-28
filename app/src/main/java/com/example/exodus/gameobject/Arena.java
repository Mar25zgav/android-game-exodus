package com.example.exodus.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import com.example.exodus.R;
import com.example.exodus.menupanel.GameActivity;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private Context context;
    private Paint paint = new Paint();
    private String[] wallColors;
    private List<Integer> colors = new ArrayList<>();
    private static List<RectF> wallList = new ArrayList<>();
    private static List<RectF> doorList = new ArrayList<>();
    private static float screenHeight = GameActivity.getScreenHeight();
    private static float screenWidth = GameActivity.getScreenWidth();
    private static float wallSize = (float) (screenHeight * 0.04);
    private float doorSize = Player.getStaticRadius() * 3;
    private int indexColor = 0;

    public Arena(Context context) {
        this.context = context;
        this.wallColors = context.getResources().getStringArray(R.array.arenaColors);
        changeColor();
        addWalls();
        addDoors();
    }

    public void draw(Canvas canvas) {
        // Draw floor
        canvas.drawColor(ContextCompat.getColor(context, R.color.background));

        // Draw walls
        for (RectF r : wallList) {
            canvas.drawRect(r, paint);
        }

        // Draw doors
        for (RectF r : doorList) {
            canvas.drawRect(r, paint);
        }
    }

    private void addWalls() {
        //Creating and adding rectangles in an array - arena
        wallList.add(new RectF(0, 0, wallSize, screenHeight / 2 - doorSize));
        wallList.add(new RectF(0, screenHeight / 2 + doorSize, wallSize, screenHeight));
        wallList.add(new RectF(0, 0, screenWidth / 2 - doorSize, wallSize));
        wallList.add(new RectF(screenWidth / 2 + doorSize, 0, screenWidth, wallSize));
        wallList.add(new RectF(screenWidth - wallSize, 0, screenWidth, screenHeight / 2 - doorSize));
        wallList.add(new RectF(screenWidth - wallSize, screenHeight / 2 + doorSize, screenWidth, screenHeight));
        wallList.add(new RectF(0, screenHeight - wallSize, screenWidth / 2 - doorSize, screenHeight));
        wallList.add(new RectF(screenWidth / 2 + doorSize, screenHeight - wallSize, screenWidth, screenHeight));
    }

    public void addDoors() {
        //Arena doors
        doorList.add(new RectF(0, screenHeight / 2 - doorSize, wallSize, screenHeight / 2 + doorSize));
        doorList.add(new RectF(screenWidth / 2 - doorSize, 0, screenWidth / 2 + doorSize, wallSize));
        doorList.add(new RectF(screenWidth - wallSize, screenHeight / 2 - doorSize, screenWidth, screenHeight / 2 + doorSize));
        doorList.add(new RectF(screenWidth / 2 - doorSize, screenHeight - wallSize, screenWidth / 2 + doorSize, screenHeight));
    }

    public void openDoors() {
        doorList.clear();
    }

    public static boolean collision(Circle obj) {
        for (RectF r : wallList) {
            if (intersects(obj, r))
                return true;
        }
        for (RectF r : doorList) {
            if (intersects(obj, r))
                return true;
        }
        return false;
    }

    private static boolean intersects(Circle obj, RectF rect) {
        boolean intersects = false;
        if (obj.getPositionX() + obj.radius >= rect.left &&
                obj.getPositionX() - obj.radius <= rect.right &&
                obj.getPositionY() - obj.radius <= rect.bottom &&
                obj.getPositionY() + obj.radius >= rect.top) {
            intersects = true;
        }
        return intersects;
    }

    public boolean leavesArena(Circle obj) {
        boolean leaves = false;
        if (obj.getPositionX() - obj.getRadius() >= screenWidth ||
                obj.getPositionX() + obj.getRadius() <= 0 ||
                obj.getPositionY() + obj.getRadius() <= 0 ||
                obj.getPositionY() - obj.getRadius() >= screenHeight) {
            leaves = true;
        }
        return leaves;
    }

    public void setPlayerPosition(Player player) {
        //Set player position coming from doors
        if (player.getPositionX() < 0 && (player.getPositionY() > screenHeight / 2 - doorSize && player.getPositionY() < screenHeight / 2 + doorSize)) {
            //left exit to right entry
            player.position.x = screenWidth - wallSize - player.getRadius();
            player.position.y = player.getPositionY();
        } else if ((player.getPositionX() > screenWidth / 2 - doorSize && player.getPositionX() < screenWidth / 2 + doorSize) && player.getPositionY() < 0) {
            //top exit to bottom entry
            player.position.x = player.getPositionX();
            player.position.y = screenHeight - wallSize - player.getRadius();
        } else if (player.getPositionX() > screenWidth && (player.getPositionY() > screenHeight / 2 - doorSize && player.getPositionY() < screenHeight / 2 + doorSize)) {
            //right exit to left entry
            player.position.x = wallSize + player.getRadius();
            player.position.y = player.getPositionY();
        } else {
            //bottom exit to top entry
            player.position.x = player.getPositionX();
            player.position.y = wallSize + player.getRadius();
        }
    }

    public void changeColor() {
        //Set wall color
        for (String wallColor : wallColors) {
            int newColor = Color.parseColor(wallColor);
            colors.add(newColor);
        }

        paint.setColor(colors.get(indexColor));

        if (indexColor >= colors.size()) {
            indexColor = 0;
        }

        // Increment index color
        if (indexColor < colors.size() - 1) {
            indexColor++;
        }

    }

    public static float getWallSize() {
        return wallSize;
    }
}
