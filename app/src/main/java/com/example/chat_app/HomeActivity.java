package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up OnClickListener for each menu item
        View navHome = bottomNavigationView.findViewById(R.id.nav_home);
        View navContacts = bottomNavigationView.findViewById(R.id.nav_contacts);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tetap di halaman Home, lakukan apa pun yang perlu dilakukan
                // Misalnya, memuat ulang atau memperbarui konten
            }
        });

        navContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Buka ContactsActivity saat item kontak diklik
                Intent intent = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        // Button to enter the chat room
        Button enterChatButton = findViewById(R.id.enterChatButton);
        enterChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainActivity when button is clicked
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
