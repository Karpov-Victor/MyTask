package struna14protocol;

public enum RequestCommand {
    connection(0x10, "Проверить связь с БВ"),                  // Проверка связи 10
    configuration(0x11, "Получить конфигурацию"),                // Конфигурация 11
    state(0x14, "Получить состояние"),                           // Состояние 14
    level(0x20, "Получить значение уровня"),                     // Параметр уровня. От 20 до 2F
    density(0x50, "Получить значение плотности"),                // Параметр плотности. От 50 до 5F
    volume(0x80, "Получить значение объёма"),                    // Параметр объёма. От 80 до 8F
    mass(0xB0, "Получить значение массы"),                       // Параметр массы. От B0 до BF
    temp(0x30, "Получить значение температуры"),                 // Параметры температуры. От 30 до 3F
    waterLevel(0x40, "Получить значение подтоварной воды"),      // Параметр подтоварной воды. От 40 до 4F
    tempUp(0x60, "Получить значение температуры верхнего ДТ");   // Параметр плотности. От 60 до 6F

    private int command;
    private String commandName;

    RequestCommand(int command, String commandName) {
        this.command = command;
        this.commandName = commandName;
    }

    public int getCommand() {
        return command;
    }

    public String getCommandName() {
        return commandName;
    }
}
