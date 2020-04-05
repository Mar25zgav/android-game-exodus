package com.example.exodus.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.exodus.Game;
import com.example.exodus.menupanel.GameActivity;
import com.example.exodus.R;

import java.util.ArrayList;
import java.util.List;

public class Arena{
    private Paint paint = new Paint();
    private List<Integer> colors = new ArrayList<Integer>();
    private String[] wallColors;
    private static List<Rect> wallList = new ArrayList<Rect>();
    private static List<Rect> doorList = new ArrayList<Rect>();
    private static int screenHeight = GameActivity.getScreenHeight();
    private static int screenWidth = GameActivity.getScreenWidth();
    private static int wallSize = (int)(screenWidth*0.025);
    private int indexDoorColor = 0;

    public Arena(Context context){
        this.wallColors = context.getResources().getStringArray(R.array.arenaColors);
        changeColor();
        addWalls();
        addDoors();
    }

    public void draw(Canvas canvas){
        // Draw walls
        for(Rect r : wallList){
            canvas.drawRect(r, paint);
        }

        // Draw doors
        for(Rect r : doorList){
            canvas.drawRect(r, paint);
        }
    }

    public void addWalls() {
        //Creating and adding rectangles in an array - arena
        wallList.add(new Rect(0,0, wallSize, screenHeight/2-100));
        wallList.add(new Rect(0, screenHeight/2+100, wallSize, screenHeight));
        wallList.add(new Rect(0,0,screenWidth/2-100, wallSize));
        wallList.add(new Rect(screenWidth/2+100, 0, screenWidth, wallSize));
        wallList.add(new Rect(screenWidth-wallSize, 0, screenWidth, screenHeight/2-100));
        wallList.add(new Rect(screenWidth-wallSize, screenHeight/2+100, screenWidth, screenHeight));
        wallList.add(new Rect(0, screenHeight-wallSize, screenWidth/2-100, screenHeight));
        wallList.add(new Rect(screenWidth/2+100, screenHeight-wallSize, screenWidth, screenHeight));
    }

    public void addDoors() {
        //Arena doors
        doorList.add(new Rect(0, screenHeight/2+-100, wallSize, screenHeight/2+100));
        doorList.add(new Rect(screenWidth/2-100, 0, screenWidth/2+100, wallSize));
        doorList.add(new Rect(screenWidth-wallSize, screenHeight/2-100, screenWidth, screenHeight/2+100));
        doorList.add(new Rect(screenWidth/2-100, screenHeight-wallSize, screenWidth/2+100, screenHeight));
    }

    public void openDoors() {
        doorList.clear();
    }

    public static boolean collision(Circle obj){
        for(Rect r : wallList){
            if(intersects(obj, r))
                return true;
        }
        for(Rect r : doorList){
            if(intersects(obj, r))
                return true;
        }
        return false;
    }

    public static boolean intersects(Circle obj, Rect rect) {
        boolean intersects = false;
        if(obj.getPositionX() + obj.radius >= rect.left &&
                obj.getPositionX() - obj.radius <= rect.right &&
                obj.getPositionY() - obj.radius <= rect.bottom &&
                obj.getPositionY() + obj.radius >= rect.top)
        {
            intersects = true;
        }
        return intersects;
    }

    public boolean leavesArena(Circle obj){
        boolean leaves = false;
        if(obj.getPositionX() - 35 >=  screenWidth ||
                obj.getPositionX() + 35 <=  0 ||
                obj.getPositionY() + 35 <=  0 ||
                obj.getPositionY() - 35 >= screenHeight)
        {
            leaves = true;
        }
        return leaves;
    }

    public void setPlayerPosition(Player player) {
        //Set player position coming from doors
        if (player.getPositionX() < 0 && (player.getPositionY() > screenHeight / 2 - 100 && player.getPositionY() < screenHeight / 2 + 100)) {
            //left exit to right entry
            player.position.x = screenWidth - wallSize - 35;
            player.position.y = player.getPositionY();
        } else if ((player.getPositionX() > screenWidth / 2 - 100 && player.getPositionX() < screenWidth / 2 + 100) && player.getPositionY() < 0) {
            //top exit to bottom entry
            player.position.x = player.getPositionX();
            player.position.y = screenHeight - wallSize - 35;
        } else if (player.getPositionX() > screenWidth && (player.getPositionY() > screenHeight / 2 - 100 && player.getPositionY() < screenHeight / 2 + 100)) {
            //right exit to left entry
            player.position.x = wallSize + 35;
            player.position.y = player.getPositionY();
        } else {
            //bottom exit to top entry
            player.position.x = player.getPositionX();
            player.position.y = wallSize + 35;
        }
    }

    public void changeColor() {
        //Set color
        for(int i = 0; i < wallColors.length; i++) {
            int newColor = Color.parseColor(wallColors[i]);
            colors.add(newColor);
        }

        paint.setColor(colors.get(indexDoorColor));

        // Increment index color
        if(indexDoorColor < colors.size()-1)
            indexDoorColor++;
    }

    public static int getWallSize() { return wallSize; }
}
