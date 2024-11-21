package networking;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Клиент подключен к серверу.");
            while (true) {
                System.out.println("Выберите фигуру (circle/rectangle) или введите 'Q' для выхода:");
                String choice = scanner.nextLine();

                if ("Q".equalsIgnoreCase(choice)) {
                    outputStream.writeObject("Q");
                    System.out.println("Выход...");
                    break;
                }

                switch (choice.toLowerCase()) {
                    case "circle" -> {
                        System.out.print("Введите радиус круга: ");
                        double radius = scanner.nextDouble();
                        scanner.nextLine(); // Очистка буфера
                        outputStream.writeObject(new Circle(radius));
                    }
                    case "rectangle" -> {
                        System.out.print("Введите ширину прямоугольника: ");
                        double width = scanner.nextDouble();
                        System.out.print("Введите высоту прямоугольника: ");
                        double height = scanner.nextDouble();
                        scanner.nextLine(); // Очистка буфера
                        outputStream.writeObject(new Rectangle(width, height));
                    }
                    default -> {
                        System.out.println("Неверный ввод. Попробуйте снова.");
                        continue;
                    }
                }


                String response = (String) inputStream.readObject();
                System.out.println("Ответ от сервера: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }
}
