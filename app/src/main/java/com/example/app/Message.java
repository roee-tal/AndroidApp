package com.example.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;

public class Message {

    private String text;

    public Message(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Comparator<Message> nameAscending = new Comparator<Message>()
    {
        @Override
        public int compare(Message shape1, Message shape2)
        {
            String name1 = shape1.getText();
            String name2 = shape2.getText();
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();

            return name1.compareTo(name2);
        }
    };
}