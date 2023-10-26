unit StrunaConst;

interface
const
checkConnection:  Byte = $10;  // Проверить связь
checkConnectionStringName:  String = 'Проверить связь с БВ';

configuration:  Byte = $11;  // Конфигурация
configurationStringName:  String = 'Конфигурация';

state:  Byte = $14;  // Состояние
stateStringName:  String = 'Состояние';

level:  Byte = $20;  // Параметр уровня. От 20 до 2F
levelStringName:  String = 'Получить значение уровня';

density:  Byte = $50;  // Параметр плотности. От 50 до 5F
densityStringName:  String = 'Получить значение плотности';

volume:  Byte = $80;  // Параметр плотности. От 80 до 8F
volumeStringName:  String = 'Получить значение объёма';

mass:  Byte = $B0;  // Параметр плотности. От B0 до BF
massStringName:  String = 'Получить значение массы';

temp:  Byte = $30;  // Параметр плотности. От 30 до 3F
tempStringName:  String = 'Получить значение температуры';

water:  Byte = $40;  // Параметр плотности. От 40 до 4F
waterStringName:  String = 'Получить значение подтоварной воды';

tempUp:  Byte = $60;  // Параметр плотности. От 60 до 6F
tempUpStringName:  String = 'Получить знач. темп. верхнего ДТ';

implementation

end.
