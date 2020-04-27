package com.example.exodus.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/* Circle is an abstract class which implements a draw method from GameObject for drawing the object as a circle. */
public abstract class Circle extends GameObject {
    protected float radius;
    private Paint paint;

    public Circle(Context context, int color, float positionX, float positionY, float radius) {
        super(positionX, positionY);

        this.radius = radius;

        // Set color of circle
        paint = new Paint();
        paint.setColor(color);
    }

    // Checks if two objects are colliding, based on position and radius
    public static boolean isColliding(Circle obj1, Circle obj2) {
        double distance = getDistanceBetweenObjects(obj1, obj2);
        double distanceToCollision = obj1.getRadius() + obj2.getRadius();
        return distance < distanceToCollision;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint);
    }

    public float getRadius() {
        return radius;
    }
}
