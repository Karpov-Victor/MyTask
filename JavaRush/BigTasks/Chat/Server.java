package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
            try {
                entry.getValue().send(message);
            } catch (IOException ex) {
                ConsoleHelper.writeMessage("Невозможно отправить сообщение" + entry.getValue().getRemoteSocketAddress());
            }
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        private Handler(Socket socket) {
            this.socket = socket;
        }

        // Этап первый - это этап рукопожатия (знакомства сервера с клиентом).
        // Метод в качестве параметра принимает соединение connection, а возвращает имя нового клиента.
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            // boolean badName = true;
            while (true) {
                // 1) Сформировать и отправить команду запроса имени пользователя
                connection.send(new Message(MessageType.NAME_REQUEST));
                // 2) Получить ответ клиента
                Message message = connection.receive();
                // 3) Проверить, что получена команда с именем пользователя
                if (message.getType() != MessageType.USER_NAME){
                    ConsoleHelper.writeMessage("Получено сообщение от " + socket.getRemoteSocketAddress() + ". Тип сообщения не соответствует протоколу.");
                    continue;
                }
                // 4) Достать из ответа имя,
                String userName = message.getData();
                if ((userName == null) || (userName == "")){
                    ConsoleHelper.writeMessage("Попытка подключения к серверу с пустым именем от " + socket.getRemoteSocketAddress());
                    continue;
                }
                // проверить, что оно не пустое и пользователь с таким именем еще не подключен (используй connectionMap)
                if (connectionMap.containsKey(userName)){
                    ConsoleHelper.writeMessage("Попытка подключения к серверу с уже используемым именем от " + socket.getRemoteSocketAddress());
                    continue;
                }
                // 5) Добавить нового пользователя и соединение с ним в connectionMap
                connectionMap.put(userName,connection);
                // 6) Отправить клиенту команду информирующую, что его имя принято
                connection.send(new Message(MessageType.NAME_ACCEPTED));
                return userName;
            }

        }

        // отправка клиенту (новому участнику) информации об остальных клиентах (участниках) чата.
        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String nameFromMap : connectionMap.keySet()) {
                if (!(userName.equals(nameFromMap))) {
                    connection.send(new Message(MessageType.USER_ADDED, nameFromMap));
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message newMessage = connection.receive();
                if (newMessage.getType() == MessageType.TEXT) {
                    String newTextMessage = String.format("%s: %s", userName, newMessage.getData());
                    Server.sendBroadcastMessage(new Message(MessageType.TEXT, newTextMessage));

                } else {
                    ConsoleHelper.writeMessage("Ошибка! Полученное сообщение не текст!");
                }
            }
        }

        public void run() {
            // Выводить сообщение, что установлено новое соединение с удаленным адресом
            ConsoleHelper.writeMessage("Установлено новое соединение с сервером" + socket.getRemoteSocketAddress());


            String newClientName = null;
            try (Connection connection = new Connection(socket)){
                // Создание Connection, используя поле socket
                // Приветствие клиента и получение его имени
                newClientName = serverHandshake(connection);
                // Рассылка сообщения всем участникам о добавлении нового клиента
                Server.sendBroadcastMessage(new Message(MessageType.USER_ADDED, newClientName));
                // Сообщение текущему участнику информации о других пользователях
                this.notifyUsers(connection, newClientName);
                // Главный цикл обработки сообщений
                serverMainLoop(connection,newClientName);
                connectionMap.remove(newClientName);
                Server.sendBroadcastMessage(new Message(MessageType.USER_REMOVED, newClientName));
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при создании соединения с сокетом = ");
            }
            ConsoleHelper.writeMessage("Cоединение с удаленным адресом (" + socket + "закрыто");
        }
    }

    public static void main(String[] args) {
        ConsoleHelper.writeMessage("Введите порт сервера:");
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Чат сервер запущен.");
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
