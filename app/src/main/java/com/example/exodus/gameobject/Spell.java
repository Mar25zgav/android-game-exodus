package com.example.exodus.gameobject;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.exodus.GameLoop;
import com.example.exodus.R;

public class Spell extends Circle {
    private static final float SPEED_PIXELS_PER_SECOND = 800;
    private static final float MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    public Spell(Context context, Player spellcaster){
        super(
            context,
            ContextCompat.getColor(context, R.color.player),
            spellcaster.getPositionX(),
            spellcaster.getPositionY(),
            10
        );

        velocity.x = spellcaster.getDirectionX() * MAX_SPEED;
        velocity.y = spellcaster.getDirectionY() * MAX_SPEED;
    }

    @Override
    public void update() {
        position.x += velocity.x;
        position.y += velocity.y;
    }
}
