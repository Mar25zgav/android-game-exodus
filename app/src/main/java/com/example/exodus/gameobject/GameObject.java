package com.example.exodus.gameobject;

import android.graphics.Canvas;

import com.example.exodus.PVector;

/* GameObject is an abstract class which is the foundation of all world objects in the game. */
public abstract class GameObject {
    protected PVector position, velocity, direction;

    public GameObject(float positionX, float positionY) {
        this.position = new PVector(positionX, positionY);
        this.velocity = new PVector(0, 0);
        this.direction = new PVector(1, 0);
    }

    protected static float getDistanceBetweenObjects(GameObject obj1, GameObject obj2) {
        return (float) Math.sqrt(
                Math.pow(obj2.getPositionX() - obj1.getPositionX(), 2) +
                        Math.pow(obj2.getPositionY() - obj1.getPositionY(), 2)
        );
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public float getDirectionX() {
        return direction.x;
    }

    public float getDirectionY() {
        return direction.y;
    }

    public PVector getPVector() {
        return position;
    }
}
