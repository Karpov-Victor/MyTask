package chat.client;

import chat.Connection;
import chat.ConsoleHelper;
import chat.Message;
import chat.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public class SocketThread extends Thread {

        @Override
        public void run() {
            String serverAddress = getServerAddress();
            int serverPort = getServerPort();

            try {
                Socket socket = new Socket(serverAddress, serverPort);
                Client.this.connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }

        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.NAME_REQUEST) {
                    String userName = getUserName();
                    Message newMessage = new Message(MessageType.USER_NAME, userName);
                    connection.send(newMessage);
                    continue;
                }
                if (message.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    break;
                }
                if ((message.getType() != MessageType.NAME_REQUEST) || (message.getType() != MessageType.NAME_ACCEPTED)) {
                    throw new IOException("Unexpected MessageType");
                }

            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                    continue;
                }
                if (message.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(message.getData());
                    continue;
                }
                if (message.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(message.getData());
                    continue;
                }
                throw new IOException("Unexpected MessageType");
            }

        }

        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(String.format("Участник с именем %s присоединился к чату.", userName));
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(String.format("Участник с именем %s покинул чат.", userName));
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            synchronized (Client.this) {
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
        }
    }

    // Запрос и возврат адреса сервера
    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Введите адрес сервера:");
        return ConsoleHelper.readString();
    }

    // Запрос и возврат порта сервера
    protected int getServerPort() {
        ConsoleHelper.writeMessage("Введите порт сервера:");
        return ConsoleHelper.readInt();
    }

    // Запрос и возврат имени пользователя
    protected String getUserName() {
        ConsoleHelper.writeMessage("Введите имя пользователя:");
        return ConsoleHelper.readString();
    }

    // Отправлять ли текст в консоль?  в данной реализации
    // клиента всегда должен возвращать true (мы всегда отправляем текст введенный в консоль)
    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    // Создание и возврат socketThread
    protected SocketThread getSocketThread() {
        SocketThread socketThread = new SocketThread();
        return socketThread;
    }

    // создает новое текстовое сообщение, используя переданный текст и отправляет его серверу через соединение connection.
    protected void sendTextMessage(String text) {
        try {
            Message message = new Message(MessageType.TEXT, text);
            connection.send(message);
        } catch (IOException e) {
            this.clientConnected = false;
            ConsoleHelper.writeMessage("Ошибка при отправке сообщения \"" + text + "\"");
        }
    }

    public void run() {
        // Object lock = new Object();
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Невозможно установить соединение с сервером");
                return;
            }
        }

        if (clientConnected) {
            ConsoleHelper.writeMessage("Соединение установлено.\nДля выхода наберите команду 'exit'.");
        } else {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");

        }
        String textMessage = null;

        while (clientConnected) {
            textMessage = ConsoleHelper.readString();
            if (textMessage.equals("exit")) {
                clientConnected = false;
                continue;
            }
            if (shouldSendTextFromConsole()) {
                sendTextMessage(textMessage);
            }
        }


    }


}
