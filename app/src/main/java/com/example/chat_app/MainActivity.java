package com.example.chat_app;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.13", 1212);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

    private void addMessageToLog(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setPadding(20, 10, 20, 10);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (message.startsWith("You:")) {
            layoutParams.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.bg_message_sent);
        } else {
            layoutParams.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.bg_message_received);
        }

        textView.setLayoutParams(layoutParams);
        messageLog.addView(textView);

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
