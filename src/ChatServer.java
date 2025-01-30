import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server started!");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected!");

            // Создаем поток для каждого клиента
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    }

    // Отправка сообщения всем клиентам
    public static void broadcastMessage(String message) {
        for (PrintWriter writer : clients) {
            writer.println(message);
        }
    }

    // Класс для обработки каждого клиента
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Добавляем клиента в список
                clients.add(out);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcastMessage(message); // Отправляем сообщение всем
                }
            } catch (IOException e) {
                System.out.println("Client disconnected.");
            } finally {
                // Удаляем клиента при отключении
                clients.remove(out);
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket.");
                }
            }
        }
    }
}