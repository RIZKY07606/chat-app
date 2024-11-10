package com.example.chat_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CursorAdapter;

public class ContactAdapter extends CursorAdapter {

    public ContactAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Bind name and phone number to the TextViews
        TextView nameTextView = view.findViewById(android.R.id.text1);
        TextView phoneTextView = view.findViewById(android.R.id.text2);

        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        nameTextView.setText(name);
        phoneTextView.setText(phoneNumber);
    }
}
