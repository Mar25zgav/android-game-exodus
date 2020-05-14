package com.example.exodus.menupanel;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.exodus.R;

public class Card extends CardView {

    public static Card newInstance(Context context, int position) {
        Card card = new Card(context);
        card.setCardElevation(10);

        TextView nameTxt = new TextView(context);
        nameTxt.setTextSize(context.getResources().getDisplayMetrics().density * 6);
        nameTxt.setGravity(Gravity.CENTER_HORIZONTAL);
        nameTxt.setTypeface(ResourcesCompat.getFont(context, R.font.titan_one));

        ImageView img = new ImageView(context);
        img.setPadding(0, (int) (context.getResources().getDisplayMetrics().density * 20), 0, 0);
        int imgWidth = (int) context.getResources().getDisplayMetrics().density * 100;
        int imgHeight = (int) context.getResources().getDisplayMetrics().density * 100;
        img.setLayoutParams(new ViewGroup.LayoutParams(imgWidth, imgHeight));

        ProgressBar damageBar = ((ShopActivity) context).findViewById(R.id.damageBar);
        ProgressBar firerateBar = ((ShopActivity) context).findViewById(R.id.firerateBar);
        ProgressBar speedBar = ((ShopActivity) context).findViewById(R.id.speedBar);

        TextView reachScore = ((ShopActivity) context).findViewById(R.id.reachScore);

        int weapons_num = MainActivity.getInstance().getWeaponsNum() - 1;

        switch (position) {
            case 0:
                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                nameTxt.setText("Pistol");
                reachScore.setText("");
                img.setImageResource(R.drawable.pistol);
                card.addView(nameTxt);
                card.addView(img);
                damageBar.setProgress(30);
                firerateBar.setProgress(40);
                speedBar.setProgress(50);
                break;
            case 1:
                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (weapons_num >= 1) {
                    nameTxt.setText("Uzi");
                    img.setImageResource(R.drawable.uzi);
                    reachScore.setText("");
                } else {
                    nameTxt.setText("Locked");
                    nameTxt.setShadowLayer(1f, 1f, 1f, Color.RED);
                    img.setImageResource(R.drawable.uzi_block);
                    reachScore.setText("Reach 1000 points to unlock!");
                }
                card.addView(nameTxt);
                card.addView(img);
                damageBar.setProgress(20);
                firerateBar.setProgress(80);
                speedBar.setProgress(60);
                break;
            case 2:
                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (weapons_num >= 2) {
                    nameTxt.setText("Musket");
                    img.setImageResource(R.drawable.musket);
                    reachScore.setText("");
                } else {
                    nameTxt.setText("Locked");
                    nameTxt.setShadowLayer(1f, 1f, 1f, Color.RED);
                    img.setImageResource(R.drawable.musket_block);
                    reachScore.setText("Reach 2000 points to unlock!");
                }
                card.addView(nameTxt);
                card.addView(img);
                damageBar.setProgress(60);
                firerateBar.setProgress(20);
                speedBar.setProgress(40);
                break;
            case 3:
                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (weapons_num >= 3) {
                    nameTxt.setText("Rifle");
                    img.setImageResource(R.drawable.rifle);
                    reachScore.setText("");
                } else {
                    nameTxt.setText("Locked");
                    nameTxt.setShadowLayer(1f, 1f, 1f, Color.RED);
                    img.setImageResource(R.drawable.rifle_block);
                    reachScore.setText("Reach 4000 points to unlock!");
                }
                card.addView(nameTxt);
                card.addView(img);
                damageBar.setProgress(65);
                firerateBar.setProgress(60);
                speedBar.setProgress(70);
                break;
            case 4:
                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (weapons_num >= 4) {
                    nameTxt.setText("Sniper");
                    img.setImageResource(R.drawable.sniper);
                    reachScore.setText("");
                } else {
                    nameTxt.setText("Locked");
                    nameTxt.setShadowLayer(1f, 1f, 1f, Color.RED);
                    img.setImageResource(R.drawable.sniper_block);
                    reachScore.setText("Reach 8000 points to unlock!");
                }
                card.addView(nameTxt);
                card.addView(img);
                damageBar.setProgress(100);
                firerateBar.setProgress(30);
                speedBar.setProgress(90);
                break;
            case 5:
                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (weapons_num >= 5) {
                    nameTxt.setText("Machine gun");
                    img.setImageResource(R.drawable.machine);
                    reachScore.setText("");
                } else {
                    nameTxt.setText("Locked");
                    nameTxt.setShadowLayer(1f, 1f, 1f, Color.RED);
                    img.setImageResource(R.drawable.machine_block);
                    reachScore.setText("Reach 10000 points to unlock!");
                }
                card.addView(nameTxt);
                card.addView(img);
                damageBar.setProgress(85);
                firerateBar.setProgress(80);
                speedBar.setProgress(80);
                break;
        }

        return card;
    }

    public Card(@NonNull Context context) {
        super(context);
    }
}
