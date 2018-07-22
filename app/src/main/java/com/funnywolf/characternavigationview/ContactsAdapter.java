package com.funnywolf.characternavigationview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by funnywolf on 18-7-22.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private List<ContactsItem> mItems;
    private Map<Character, Integer> mLetterIndex;

    public ContactsAdapter(List<String> names) {
        mLetterIndex = new HashMap<>();
        mItems = sortAndInsertLetter(names);
    }

    private List<ContactsItem> sortAndInsertLetter(List<String> names) {
        List<ContactsItem> items = new ArrayList<>();
        if(names == null || names.size() == 0) {
            return items;
        }
        TreeMap<Character, TreeMap<String, ContactsItem>> allMap = new TreeMap<>();
        for (int i = 0; i < names.size(); i++) {
            char c = getHeaderLetter(names.get(i));
            TreeMap<String, ContactsItem> map = allMap.get(c);
            if(map == null) {
                map = new TreeMap<>();
                allMap.put(c, map);
            }
            map.put(names.get(i) + i, new ContactsItem(names.get(i)));
        }
        char letter = 0;
        for (Character c : allMap.keySet()) {
            if(c == '#') {
                continue;
            }
            if(letter != c) {
                letter = c;
                mLetterIndex.put(letter, items.size());
                items.add(new ContactsItem(letter));
            }
            items.addAll(allMap.get(c).values());
        }
        if (allMap.containsKey('#')) {
            mLetterIndex.put('#', items.size());
            items.add(new ContactsItem('#'));
            items.addAll(allMap.get('#').values());
        }
        return items;
    }

    private char getHeaderLetter(String string) {
        if(TextUtils.isEmpty(string)) {
            return '#';
        }
        char c = string.charAt(0);
        if(c >= 'A' && c <= 'Z') {
            return c;
        }
        if(c >= 'a' && c <= 'z') {
            return (char) (c - 'a' + 'A');
        }
        return '#';
    }

    public Map<Character, Integer> getLetterIndex() {
        return mLetterIndex;
    }

    public List<ContactsItem> getContactsItem() {
        return mItems;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ContactsItem.TYPE_LETTER:
                holder.mTvLetter.setVisibility(View.VISIBLE);
                holder.mTvName.setVisibility(View.GONE);
                holder.mTvLetter.setText(String.valueOf(mItems.get(position).letter));
                break;
            case ContactsItem.TYPE_NAME:
                holder.mTvLetter.setVisibility(View.GONE);
                holder.mTvName.setVisibility(View.VISIBLE);
                holder.mTvName.setText(mItems.get(position).name);
                break;
            default:
                holder.mTvLetter.setVisibility(View.GONE);
                holder.mTvName.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView mTvLetter;
        TextView mTvName;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            mTvLetter = itemView.findViewById(R.id.tv_letter);
            mTvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
