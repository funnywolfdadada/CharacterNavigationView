package com.funnywolf.characternavigationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements CharacterNavigationView.OnTouchCharListener{
    private static final String TAG = "MainActivity";

    private CharacterNavigationView mCnvChars;
    private List<Character> mChars;

    private RecyclerView mRvContacts;
    private ContactsAdapter mContactsAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<String> mContacts;
    private Map<Character, Integer> mLetterIndex;
    private int mIndex;
    private boolean mNeedMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        initContacts();
        initNavigation();
    }

    private void initNavigation() {
        initNavigationChars();
        mCnvChars = findViewById(R.id.cnv_chars);
        mCnvChars.setChars(mChars);
        mCnvChars.setOnTouchCharListener(this);
    }

    private void initNavigationChars() {
        mChars = new ArrayList<>();
        for(Character c = 'A'; c <= 'Z'; c++) {
            mChars.add(c);
        }
        mChars.add('#');
    }

    private void initContacts() {
        initContactsName();
        mRvContacts = findViewById(R.id.rv_contacts);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvContacts.setLayoutManager(mLinearLayoutManager);
        mRvContacts.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mContactsAdapter = new ContactsAdapter(mContacts);
        mLetterIndex = mContactsAdapter.getLetterIndex();
        mRvContacts.setAdapter(mContactsAdapter);
        mRvContacts.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                if(mNeedMove) {
                    mNeedMove = false;
                    int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if(0 < n && n < mRvContacts.getChildCount()) {
                        mRvContacts.scrollBy(0, mRvContacts.getChildAt(n).getTop());
                    }
                }
            }
        });
    }

    private void initContactsName() {
        mContacts = new ArrayList<>();
        Random random = new Random(17);
        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(10) + 3;
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if(random.nextBoolean()) {
                    builder.append((char) ('A' + random.nextInt(26)));
                }else {
                    builder.append((char) ('a' + random.nextInt(26)));
                }
            }
            mContacts.add(builder.toString());
        }
        mContacts.add("!@#$%^&");
        mContacts.add("%^&");
        mContacts.add("(*&^");
        mContacts.add("}{:>>:");
        mContacts.add("&*()P:");
    }

    @Override
    public void onTouchChar(int position) {
        if(position < 0 || position >= mChars.size()) {
            return;
        }
        char c = mChars.get(position);
        if(!mLetterIndex.containsKey(c)) {
            return;
        }
        mIndex = mLetterIndex.get(c);
        int first = mLinearLayoutManager.findFirstVisibleItemPosition();
        int last = mLinearLayoutManager.findLastVisibleItemPosition();
        Log.d(TAG, "onTouchChar: " + mIndex + ", " + first + ", " + last);
        if(mIndex <= first) {
            mRvContacts.scrollToPosition(mIndex);
        }else if(mIndex <= last) {
            mRvContacts.scrollBy(0, mRvContacts.getChildAt(mIndex - first).getTop());
        }else {
            mRvContacts.scrollToPosition(mIndex);
            mNeedMove = true;
        }
    }
}
