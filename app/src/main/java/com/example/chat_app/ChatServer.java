package com.example.chat_app;

import java.io.*;
import java.net.*;

public class ChatServer {
    private ServerSocket serverSocket;
    private Socket socket1;
    private Socket socket2;

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
            System.out.println("Server berjalan di port " + port);

            // Tunggu dua client untuk terhubung
            socket1 = serverSocket.accept();
            System.out.println("Client 1 terhubung");

            socket2 = serverSocket.accept();
            System.out.println("Client 2 terhubung");

            // Mulai thread untuk menangani kedua client
            new Thread(new ClientHandler(socket1, socket2)).start();
            new Thread(new ClientHandler(socket2, socket1)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            if (socket1 != null) socket1.close();
            if (socket2 != null) socket2.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Socket otherClientSocket;

    public ClientHandler(Socket clientSocket, Socket otherClientSocket) {
        this.clientSocket = clientSocket;
        this.otherClientSocket = otherClientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(otherClientSocket.getOutputStream(), true)) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Pesan diterima: " + message);
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
