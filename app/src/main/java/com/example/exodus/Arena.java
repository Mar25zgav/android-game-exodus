package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Arena{

    private Paint paint;
    private Player player;
    private static List<Rect> wallList = new ArrayList<Rect>();
    private static List<Rect> doorList = new ArrayList<Rect>();
    private static List<Integer> colors = new ArrayList<Integer>();
    private static String[] wallColors;
    public static int screenHeight, screenWidth, wallSize;
    public static int indexDoorColor;
    private double positionX, positionY;

    public Arena(Context context, Player player){
        this.player = player;
        this.wallColors = context.getResources().getStringArray(R.array.arenaColors);
        this.indexDoorColor = 0;

        //Set color
        for(int i = 0; i < wallColors.length; i++) {
            int newColor = Color.parseColor(wallColors[i]);
            colors.add(newColor);
        }
        paint = new Paint();

        screenHeight = MainActivity.getScreenHeight();
        screenWidth = MainActivity.getScreenWidth();
        wallSize = (int)(screenWidth*0.03);

        //Creating and adding rectangles in an array - arena
        wallList.add(new Rect(0,0, wallSize, screenHeight/2-100));
        wallList.add(new Rect(0, screenHeight/2+100, wallSize, screenHeight));
        wallList.add(new Rect(0,0,screenWidth/2-100, wallSize));
        wallList.add(new Rect(screenWidth/2+100, 0, screenWidth, wallSize));
        wallList.add(new Rect(screenWidth-wallSize, 0, screenWidth, screenHeight/2-100));
        wallList.add(new Rect(screenWidth-wallSize, screenHeight/2+100, screenWidth, screenHeight));
        wallList.add(new Rect(0, screenHeight-wallSize, screenWidth/2-100, screenHeight));
        wallList.add(new Rect(screenWidth/2+100, screenHeight-wallSize, screenWidth, screenHeight));

        //Arena doors
        doorList.add(new Rect(0, screenHeight/2+-100, wallSize, screenHeight/2+100));
        doorList.add(new Rect(screenWidth/2-100, 0, screenWidth/2+100, wallSize));
        doorList.add(new Rect(screenWidth-wallSize, screenHeight/2-100, screenWidth, screenHeight/2+100));
        doorList.add(new Rect(screenWidth/2-100, screenHeight-wallSize, screenWidth/2+100, screenHeight));
    }

    public void draw(Canvas canvas){
        paint.setColor(colors.get(indexDoorColor));
        for(Rect r : wallList){
            canvas.drawRect(r, paint);
        }
        for(Rect r : doorList){
            canvas.drawRect(r, paint);
        }
    }

    public void update(){
        if(Game.enemiesKilled >= 10){
            Game.enemiesKilled = 0;
            doorList.clear();
        }

        if(leavesScreen(player)){
            if(indexDoorColor < colors.size()-1)
                indexDoorColor++;
            doorList.add(new Rect(0, screenHeight/2+-100, wallSize, screenHeight/2+100));
            doorList.add(new Rect(screenWidth/2-100, 0, screenWidth/2+100, wallSize));
            doorList.add(new Rect(screenWidth-wallSize, screenHeight/2-100, screenWidth, screenHeight/2+100));
            doorList.add(new Rect(screenWidth/2-100, screenHeight-wallSize, screenWidth/2+100, screenHeight));

            positionX = player.positionX;
            positionY = player.positionY;
            //Set player position coming from doors
            if(positionX < 0 && (positionY > screenHeight/2-100 && positionY < screenHeight/2+100)){
                //left exit to right entry
                player.positionX = screenWidth-wallSize-30;
                player.positionY = positionY;
            }else if((positionX > screenWidth/2-100 && positionX < screenWidth/2+100) && positionY < 0){
                //top exit to bottom entry
                player.positionX = positionX;
                player.positionY = screenHeight-wallSize-30;
            }else if(positionX > screenWidth && (positionY > screenHeight/2-100 && positionY < screenHeight/2+100)){
                //right exit to left entry
                player.positionX = wallSize+30;
                player.positionY = positionY;
            }else{
                //bottom exit to top entry
                player.positionX = positionX;
                player.positionY = wallSize+30;
            }

            Game.enemyList.clear();
            if(Player.health < 5)
                Player.health += 1;
        }
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

    public static boolean leavesScreen(Circle obj){
        boolean leaves = false;
        if(obj.getPositionX() - 30 >=  screenWidth||
                obj.getPositionX() + 30 <=  0||
                obj.getPositionY() + 30 <=  0||
                obj.getPositionY() - 30 >= screenHeight)
        {
            leaves = true;
        }
        return leaves;
    }
}
