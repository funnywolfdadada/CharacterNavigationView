package com.funnywolf.characternavigationview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A character navigation view
 * Created by funnywolf on 18-7-21.
 */

public class CharacterNavigationView extends RelativeLayout
        implements RecyclerView.OnItemTouchListener{

    private static final String TAG = "CharacterNavigationView";

    private RecyclerView mRvChars;
    private TextView mTvChar;
    private CharsAdapter mAdapter;
    private int mLastTouchPosition = Integer.MAX_VALUE;
    private OnTouchCharListener mListener;

    public CharacterNavigationView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CharacterNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CharacterNavigationView(@NonNull Context context, @Nullable AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CharacterNavigationView(@NonNull Context context, @Nullable AttributeSet attrs,
                                   int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(@NonNull Context context) {
        LayoutInflater.from(context).inflate(R.layout.character_navigation_layout, this);
        mRvChars = findViewById(R.id.rv_chars);
        mTvChar = findViewById(R.id.tv_char);
        mRvChars.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new CharsAdapter();
        mRvChars.setAdapter(mAdapter);
        mRvChars.addOnItemTouchListener(this);
    }

    private void onTouchChar(int position) {
        if(position == mLastTouchPosition) {
            return;
        }
        Log.d(TAG, "onTouchCharIndex: " + position);
        if(mListener != null) {
            mListener.onTouchChar(position);
        }
        if(position >= 0 && position < mAdapter.getChars().size()) {
            mTvChar.setText(String.valueOf(mAdapter.getChars().get(position)));
            mTvChar.setVisibility(VISIBLE);
            setActivated(true);
        }else {
            mTvChar.setVisibility(GONE);
            setActivated(false);
        }
        mLastTouchPosition = position;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return rv.findChildViewUnder(e.getX(), e.getY()) != null;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP || rv.getChildAt(0) == null) {
            onTouchChar(-1);
            return;
        }
        onTouchChar( rv.getChildLayoutPosition(rv.findChildViewUnder(
                rv.getChildAt(0).getX(), e.getY() )) );
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void setChars(List<Character> chars) {
        mAdapter.setChars(chars);
    }

    public void setChars(Character[] chars) {
        mAdapter.setChars(chars);
    }

    public void setOnTouchCharListener(OnTouchCharListener listener) {
        mListener = listener;
    }

    private static class CharsAdapter extends RecyclerView.Adapter<CharViewHolder> {
        private List<Character> mChars;

        public CharsAdapter() {
            mChars = new ArrayList<>();
        }

        @NonNull
        @Override
        public CharViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_item, null);
            return new CharViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CharViewHolder holder, int position) {
            ((TextView) holder.itemView).setText( String.valueOf(mChars.get(position)) );
        }

        @Override
        public int getItemCount() {
            return mChars.size();
        }

        public void setChars(List<Character> chars) {
            mChars = chars == null ? new ArrayList<Character>() : chars;
            notifyDataSetChanged();
        }

        public void setChars(Character[] chars) {
            mChars.clear();
            mChars.addAll(Arrays.asList(chars));
            notifyDataSetChanged();
        }

        public List<Character> getChars() {
            return mChars;
        }
    }

    private static class CharViewHolder extends RecyclerView.ViewHolder {
        public CharViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnTouchCharListener {
        void onTouchChar(int position);
    }
}
