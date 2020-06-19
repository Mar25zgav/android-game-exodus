package com.example.exodus.menupanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.exodus.R;

import java.util.ArrayList;

public class ShopActivity extends Activity {
    ArrayList<Card> weapons = new ArrayList<>();
    Button prevBtn, nextBtn;
    LinearLayout layout;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.shop_layout);
        getWindow().setLayout((int) (MainActivity.getScreenWidth() * .70), (int) (MainActivity.getScreenHeight() * .85));

        layout = findViewById(R.id.content);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn.setVisibility(View.INVISIBLE);

        for (int i = 0; i < 5; i++) {
            weapons.add(Card.newInstance(this, i));
        }

        layout.addView(Card.newInstance(this, i));

        nextBtn.setOnClickListener(v -> {
            if (i < weapons.size()) {
                i += 1;
                layout.removeAllViews();
                layout.addView(Card.newInstance(ShopActivity.this, i));

                if (i == weapons.size()) {
                    nextBtn.setVisibility(View.INVISIBLE);
                    prevBtn.setVisibility(View.VISIBLE);
                } else {
                    nextBtn.setVisibility(View.VISIBLE);
                    prevBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        prevBtn.setOnClickListener(v -> {
            if (i > 0) {
                i -= 1;
                layout.removeAllViews();
                layout.addView(Card.newInstance(ShopActivity.this, i));

                if (i == 0) {
                    prevBtn.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                } else {
                    prevBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
