package com.example.exodus;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class Spell extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    public Spell(Context context, Player spellcaster){
        super(
            context,
            ContextCompat.getColor(context, R.color.player),
            spellcaster.getPositionX(),
            spellcaster.getPositionY(),
            10
        );

        velocityX = spellcaster.getDirectionX()*MAX_SPEED;
        velocityY = spellcaster.getDirectionY()*MAX_SPEED;
    }

    @Override
    public void update() {
        positionX = positionX + velocityX;
        positionY = positionY + velocityY;
    }
}
