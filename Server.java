package networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


abstract class Shape implements Serializable {
    abstract double calculateArea();
}

class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    double calculateArea() {
        return width * height;
    }
}

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Сервер запущен...");
            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {

                    System.out.println("Клиент подключен...");

                    Object receivedObject = inputStream.readObject();
                    if (receivedObject instanceof Shape) {
                        Shape shape = (Shape) receivedObject;
                        double area = shape.calculateArea();
                        outputStream.writeObject("Площадь: " + area);
                    } else if ("Q".equals(receivedObject)) {
                        System.out.println("Клиент запросил закрытие соединения.");
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Неизвестный объект: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }
}
