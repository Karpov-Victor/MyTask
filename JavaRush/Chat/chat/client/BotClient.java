package chat.client;


import chat.ConsoleHelper;
import chat.Message;
import chat.MessageType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BotClient extends Client{
    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            String[] strArr = message.split(":");
            if (strArr.length != 2) return;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("YYYY");
            String command = strArr[1].trim();
            boolean isCorrectQuesion = false;
            switch (command) {
                case ("дата") :
                    dateFormat = new SimpleDateFormat("d.MM.YYYY");
                    isCorrectQuesion = true;
                    break;
                case ("день") :
                    dateFormat = new SimpleDateFormat("d");
                    isCorrectQuesion = true;
                    break;
                case ("месяц") :
                    dateFormat = new SimpleDateFormat("MMMM");
                    isCorrectQuesion = true;
                    break;
                case ("год") :
                    dateFormat = new SimpleDateFormat("YYYY");
                    isCorrectQuesion = true;
                    break;
                case ("время") :
                    dateFormat = new SimpleDateFormat("H:mm:ss");
                    isCorrectQuesion = true;
                    break;
                case ("час") :
                    dateFormat = new SimpleDateFormat("H");
                    isCorrectQuesion = true;
                    break;
                case ("минуты") :
                    dateFormat = new SimpleDateFormat("m");
                    isCorrectQuesion = true;
                    break;
                case ("секунды") :
                    dateFormat = new SimpleDateFormat("s");
                    isCorrectQuesion = true;
                    break;

            }
            if (isCorrectQuesion) sendTextMessage(String.format("Информация для %s: %s",strArr[0], dateFormat.format(date)));
        }
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        System.out.println(botClient.getUserName());
        botClient.run();
    }

    @Override
    protected SocketThread getSocketThread() {
        BotSocketThread botSocketThread = new BotSocketThread();
        return botSocketThread;
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {

        return "date_bot_"+ ((int) (Math.random() * 100));
    }


}
