package com.funnywolf.characternavigationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CharacterNavigationView mCnvChars;
    List<Character> mChars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        initNavigation();
    }

    private void initNavigation() {
        mCnvChars = findViewById(R.id.cnv_chars);
        mChars.add('^');
        for(Character c = 'A'; c < 'Z'; c++) {
            mChars.add(c);
        }
        mChars.add('#');
        mCnvChars.setChars(mChars);
    }
}
