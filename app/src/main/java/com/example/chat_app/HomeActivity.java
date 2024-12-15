package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            } else if (item.getItemId() == R.id.nav_call) {
                selectedFragment = new CallFragment();
            } else if (item.getItemId() == R.id.nav_contacts) {
                selectedFragment = new ContactsFragment();
                Intent contactsIntent = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(contactsIntent);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }

            return true;
        });

//        // Set up OnClickListener for each menu item
//        View navHome = bottomNavigationView.findViewById(R.id.nav_home);
//        View navContacts = bottomNavigationView.findViewById(R.id.nav_contacts);
//
//        navHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Tetap di halaman Home
//            }
//        });
//
//        navContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Buka ContactsActivity saat item kontak diklik
//                Intent intent = new Intent(HomeActivity.this, ContactActivity.class);
//                startActivity(intent);
//            }
//        });

        Button enterChatButton = findViewById(R.id.enterChatButton);
        enterChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
