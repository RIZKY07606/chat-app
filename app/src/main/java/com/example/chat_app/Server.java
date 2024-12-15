import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
  private static final int PORT = 1212;
  private static int clientCounter = 1;
  private static List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Server is listening on port " + PORT);

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("New client connected");

        String clientName = "Client" + clientCounter++;
        ClientHandler clientHandler = new ClientHandler(socket, clientName);
        clients.add(clientHandler);
        clientHandler.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void broadcast(String message) {
    for (ClientHandler client : clients) {
      client.sendMessage(message);
    }
  }
}

class ClientHandler extends Thread {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private String clientName;

  public ClientHandler(Socket socket, String clientName) {
    this.socket = socket;
    this.clientName = clientName;
  }

  @Override
  public void run() {
    try {
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      System.out.println(clientName + " has joined.");
      Server.broadcast(clientName + " has joined the chat.");

      String clientMessage;
      while ((clientMessage = in.readLine()) != null) {
        System.out.println("Received from " + clientName + ": " + clientMessage);

        Server.broadcast(clientName + ": " + clientMessage);

        if (clientMessage.equalsIgnoreCase("exit")) {
          System.out.println(clientName + " disconnected.");
          Server.broadcast(clientName + " has left the chat.");
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendMessage(String message) {
    if (out != null) {
      out.println(message);
    }
  }
}
