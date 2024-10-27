package com.example.chat_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText editTextMessage;
    private TextView textViewChat;
    private PrintWriter out;
    private ChatServer chatServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMessage = findViewById(R.id.editTextMessage);
        textViewChat = findViewById(R.id.textViewChat);
        Button buttonSend = findViewById(R.id.buttonSend);

        chatServer = new ChatServer();
        new Thread(() -> chatServer.startServer(12345)).start(); // Jalankan server di thread terpisah

        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            sendMessage(message);
            editTextMessage.setText("");
        });

        // Buat thread untuk menerima pesan
        new Thread(this::receiveMessages).start();
    }

    private void sendMessage(final String message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (out != null) {
                    out.println(message);
                    out.flush();
                } else {
                    Log.e("ChatApp", "Output stream tidak tersedia!");
                }
                return null;
            }
        }.execute();
    }

    private void receiveMessages() {
        try {
            // Ganti dengan alamat IP lokal komputer Anda
            Socket socket = new Socket("127.0.0.1", 12345); // Ganti dengan IP komputer
            Log.d("ChatApp", "Socket terhubung ke server");

            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                String finalMessage = message;
                runOnUiThread(() -> textViewChat.append("Client: " + finalMessage + "\n"));
            }
        } catch (IOException e) {
            Log.e("ChatApp", "Error saat mencoba terhubung: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatServer.stopServer(); // Hentikan server saat aplikasi dihentikan
    }
}
