package answer;

public enum Byte1Answer {
    confirmAndExec(0x00, "Подтверждение приёма и выполнения команды."),                      // Подтверждение приёма и выполнения команды
    unknownError(0x03, "Неизвестная ошибка"),
    defectChannelOrCommand(0x04, "Неисправность канала или параметра."),                     // Неисправность канала или параметра
    connectionError(0x06, "Ошибка связи."),                                                  // Ошибка связи
    unknownCommand(0x0C, "Неопознанная команда"),                                           // Неопознанная команда
    initialization(0xFE, "Инициализация (режим инициализации системы)"),                    // Инициализация (режим инициализации системы)
    missChannelOrCommand(0xFF, "Отсутствие канала или параметра в конфигурации системы");   // Отсутствие канала или параметра в конфигурации системы
    private int answer;
    private String answerName;

    Byte1Answer(int answer, String answerName) {
        this.answer = answer;
        this.answerName = answerName;
    }

    public String getAnswerName() {
        return answerName;
    }

    public static String getAnswerName(int errorCode) {
        switch (errorCode) {
            case  0x04: return Byte1Answer.defectChannelOrCommand.answerName;
            case  0x06: return Byte1Answer.connectionError.answerName;
            case  0x0C: return Byte1Answer.unknownCommand.answerName;
            case  0xFE: return Byte1Answer.initialization.answerName;
            case  0xFF: return Byte1Answer.missChannelOrCommand.answerName;
            default: return Byte1Answer.unknownError.answerName;
        }
    }
}
