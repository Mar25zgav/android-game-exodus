package com.example.exodus.menupanel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.example.exodus.R;

import java.lang.reflect.Field;

public class OptionsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private Switch musicSwitch;
    private Spinner diffSpinner;

    private String SHARED_PREFS = "sharedPrefs";
    private String MUSICSWITCH = "musicSwitch";
    private String DIFFSPINNER = "diffSpinner";

    private boolean musicSwitchOnOff;
    private int diffSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();

        setContentView(R.layout.options_layout);

        getWindow().setLayout((int) (MainActivity.getScreenWidth() * .55), (int) (MainActivity.getScreenHeight() * .85));

        // Music switch
        musicSwitch = findViewById(R.id.musicSwitch);

        // Difficulty spinner
        diffSpinner = findViewById(R.id.diffSpinner);
        avoidSpinnerDropdownFocus(diffSpinner); //maintain immersive mode
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffSpinner.setAdapter(adapter);
        diffSpinner.setOnItemSelectedListener(this);

        // Update views from SharedPreferences
        loadData();
        updateViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Save music switch and spinner state
        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save music switch setting
        editor.putBoolean(MUSICSWITCH, musicSwitch.isChecked());

        // Save difficulty setting
        diffSelected = diffSpinner.getSelectedItemPosition();
        editor.putInt(DIFFSPINNER, diffSelected);

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // Load music switch setting
        musicSwitchOnOff = sharedPreferences.getBoolean(MUSICSWITCH, false);

        // Load difficulty setting
        diffSelected = sharedPreferences.getInt(DIFFSPINNER, 0);
    }

    private void updateViews() {
        // Update music switch setting
        musicSwitch.setChecked(musicSwitchOnOff);

        // Update difficulty setting
        diffSpinner.setSelection(diffSelected);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void avoidSpinnerDropdownFocus(Spinner spinner) {
        try {
            Field listPopupField = Spinner.class.getDeclaredField("mPopup");
            listPopupField.setAccessible(true);
            Object listPopup = listPopupField.get(spinner);
            if (listPopup instanceof ListPopupWindow) {
                Field popupField = ListPopupWindow.class.getDeclaredField("mPopup");
                popupField.setAccessible(true);
                Object popup = popupField.get((ListPopupWindow) listPopup);
                if (popup instanceof PopupWindow) {
                    ((PopupWindow) popup).setFocusable(false);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
