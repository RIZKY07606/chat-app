package com.example.chat_app;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContactActivity extends AppCompatActivity {

    private ListView contactsListView;
    //private ImageButton buttonCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up OnClickListener for each menu item
        View navHome = bottomNavigationView.findViewById(R.id.nav_home);
        View navContacts = bottomNavigationView.findViewById(R.id.nav_contacts);
        View navCall = bottomNavigationView.findViewById(R.id.nav_call);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        navContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tetap di halaman Contact, lakukan apa pun yang perlu dilakukan
                // Misalnya, memuat ulang atau memperbarui konten
            }
        });

        navCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhoneNumber != null) {
                    makeCall(selectedPhoneNumber);
                } else {
                    Toast.makeText(ContactActivity.this, "Pilih nomor telepon", Toast.LENGTH_SHORT).show();
                }
            }
        });

        contactsListView = findViewById(R.id.contactsListView);
        //buttonCall = findViewById(R.id.buttonCall);

        // Mendapatkan daftar kontak dan menampilkannya di ListView
        loadContacts();

        // Fungsi untuk membuka kontak dan melakukan panggilan
//        buttonCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phoneNumber = getSelectedPhoneNumber(); // Dapatkan nomor telepon yang dipilih
//
//                if (phoneNumber != null) {
//                    makeCall(phoneNumber);
//                } else {
//                    Toast.makeText(ContactActivity.this, "Pilih nomor telepon", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    // Fungsi untuk mengambil kontak dari perangkat dan menampilkan di ListView
    private void loadContacts() {
        // Mendapatkan data kontak dari ContentProvider
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, // Menampilkan semua kolom
                null, // Tidak ada filter
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        // Menggunakan custom adapter untuk menampilkan data kontak
        ContactAdapter adapter = new ContactAdapter(this, cursor);
        contactsListView.setAdapter(adapter);

        // Menambahkan listener pada item yang dipilih di ListView
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                setSelectedPhoneNumber(phoneNumber); // Menyimpan nomor telepon yang dipilih
            }
        });
    }

    // Menyimpan nomor telepon yang dipilih
    private String selectedPhoneNumber = null;

    private void setSelectedPhoneNumber(String phoneNumber) {
        selectedPhoneNumber = phoneNumber;
    }

    // Fungsi untuk mendapatkan nomor telepon yang dipilih
    private String getSelectedPhoneNumber() {
        return selectedPhoneNumber;
    }

    // Fungsi untuk memulai panggilan menggunakan Intent
    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            }
        } else {
            startActivity(intent);
        }
    }
}