package com.funnywolf.characternavigationview;

/**
 * Created by funnywolf on 18-7-22.
 */

public class ContactsItem {

    public static final int TYPE_LETTER = 1;
    public static final int TYPE_NAME = 2;

    char letter;
    String name;

    public ContactsItem(char letter) {
        this.letter = letter;
        this.name = null;
    }

    public ContactsItem(String name) {
        this.letter = 0;
        this.name = name;
    }

    public int getType() {
        if(letter != 0) {
            return TYPE_LETTER;
        }
        if(name != null) {
            return TYPE_NAME;
        }
        return 0;
    }
}
