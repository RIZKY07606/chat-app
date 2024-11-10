package com.example.chat_app;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private LinearLayout messageLog;
    private EditText editTextMessage;
    private ScrollView scrollViewMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageLog = findViewById(R.id.messageLog);
        editTextMessage = findViewById(R.id.editTextMessage);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);
        Button buttonSend = findViewById(R.id.buttonSend);

        // Allow network operations on the main thread (not recommended for production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Start a new thread to connect to the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Connect to the server using the IP address and port
                    socket = new Socket("10.0.2.2", 1212);  // Emulator IP for local server
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // Receive messages from the server
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        final String message = serverMessage;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addMessageToLog(message);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Set up the send button to send a message
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientMessage = editTextMessage.getText().toString();
                if (!clientMessage.isEmpty()) {
                    out.println(clientMessage);
                    addMessageToLog("You: " + clientMessage);
                    editTextMessage.setText("");
                }
            }
        });
    }

    // Method to add a message to the chat log
    private void addMessageToLog(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        messageLog.addView(textView);

        // Scroll to the bottom of the messages
        scrollViewMessages.post(new Runnable() {
            @Override
            public void run() {
                scrollViewMessages.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
