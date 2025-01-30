import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Connected to server!");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);

        // Поток для чтения сообщений от сервера
        Thread readMessages = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Отключились от сервера.");
                }
            }
        });
        readMessages.start(); // Запускаем поток

        // Чтение сообщений от пользователя и отправка на сервер
        while (true) {
            String userInput = scanner.nextLine();
            out.println(userInput);

            if (userInput.equalsIgnoreCase("exit")) {
                break;
            }
        }

        socket.close();
        System.out.println("Disconnected from server.");
    }
}