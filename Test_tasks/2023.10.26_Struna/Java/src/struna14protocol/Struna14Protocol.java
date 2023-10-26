package struna14protocol;

import answer.BitUtils;
import answerexception.BadAnswerException;
import answerexception.ControlSummException;
import answerexception.StrunaAnswerException;
import answerexception.TimeOutAnswerException;
import answer.ChannelConfiguration;
import jssc.*;
// https://micro-pi.ru/jssc-работаем-com-портом-java-raspberry/
// https://www.ap-impulse.com/com-port-opros-i-vyvod-dannyx-na-java-shag-46/
// получить список портов String[] portNames = SerialPortList.getPortNames();

public class Struna14Protocol {
    private SerialPort serialPort;

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public static void main(String[] args) throws SerialPortException {
        // Получаем список портов
        String[] portNames = SerialPortList.getPortNames();

        // Определяем список портов, которые существуют в ОС
        System.out.println("В системе существуют следующие COM-порты:");
        for (String portName : portNames) {
            System.out.println(portName);
        }

        // Создаём объект COM-порт и на его основе создаём объект для работы с портом
        // Struna14Protocol struna14 = new Struna14Protocol(new SerialPort("COM4"));
        Struna14Protocol struna14 = new Struna14Protocol();
        struna14.setSerialPort(new SerialPort("COM3"));
        if (struna14.openPort()) System.out.println("открыто успешно");
            else System.out.println("не открыто");

        try {
            // System.out.println("Проверка контрольной суммы = " + checkControlSumm(new int[]{0x00, 0x5F, 0x79, 0x25}));
            System.out.println("---");
            System.out.println("Проверка связи. Ответ:");
            struna14.checkConnection(true, 1000);
            System.out.println("---");
            System.out.println("Получить конфигурацию. Ответ:");
            struna14.getChannelConfiguration(true, 1000);
            System.out.println("---");
            System.out.println("Получить конфигурацию по каналу. Ответ:");
            struna14.getChannelConfiguration(true, 1000,2);
            System.out.println("---");
            System.out.println("Получить состояние БВ. Ответ:");
            struna14.getState(true, 1000);
            System.out.println("---");
            System.out.println("Получить уровень. Ответ:");
            struna14.getLevel(true, 1000, 0);
            struna14.getLevel(true, 1000, 1);
            struna14.getLevel(true, 1000, 2);
            System.out.println("---");
            System.out.println("Получить плотность. Ответ:");
            struna14.getDensity(true, 1000, 0);
            struna14.getDensity(true, 1000, 1);
            struna14.getDensity(true, 1000, 2);
            System.out.println("---");
            System.out.println("Получить объём. Ответ:");
            struna14.getVolume(true, 1000, 0);
            struna14.getVolume(true, 1000, 1);
            struna14.getVolume(true, 1000, 2);
            System.out.println("---");
            System.out.println("Получить массу. Ответ:");
            struna14.getMass(true, 1000, 0);
            struna14.getMass(true, 1000, 1);
            struna14.getMass(true, 1000, 2);
            System.out.println("---");
            System.out.println("Получить температуру. Ответ:");
            struna14.getTemp(true, 1000, 0);
            System.out.println("---");
            System.out.println("Получить уровень подтоварной воды. Ответ:");
            struna14.getWaterLevel(true, 1000, 0);
            System.out.println("---");
            System.out.println("Получить температуру верхнего датчика. Ответ:");
            struna14.getTempUp(true, 1000, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

       //  System.out.println(struna14.checkConnection(1000));
    }

    public boolean openPort() {
        try {
            serialPort.openPort (); /*Метод открытия порта*/
            serialPort.setParams (SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); /*Задаем основные параметры протокола UART*/
            serialPort.setEventsMask (SerialPort.MASK_RXCHAR); /*Устанавливаем маску или список события на которые будет происходить реакция. В данном случае это приход данных в буффер порта*/
            return true;
            // struna14.serialPort.addEventListener (new EventListener (struna14)); /*Передаем экземпляр класса EventListener порту, где будет обрабатываться события. Ниже описан класс*/
        }
        catch (SerialPortException ex) {
            System.out.println (ex);
            return false;
        }
    }

    public boolean closePort() {
        try {
            serialPort.closePort(); /*Метод закрытия порта*/
            return true;
        }
        catch (SerialPortException ex) {
            System.out.println (ex);
            return false;
        }
    }


    /**
     * Отправка команды в Com-порт
     * @param command
     * @throws SerialPortException
     */
    public void sendCommandToPort(int command) throws SerialPortException {
        try {
            while ((this.serialPort.readIntArray(1, 1))[0] > 0) {

            }
        } catch (SerialPortTimeoutException e) {
        }

        this.serialPort.writeInt(command);
    }

    /**
     *
     * @param timeOut - максимальное время ожидания ответа (мс)
     * @return
     * @throws SerialPortException
     * @throws TimeOutAnswerException - выбрасывается, если превышен интервал ожидания ответа
     */
    public int[] getDataFromPort (int byteCount, int timeOut) throws SerialPortException, TimeOutAnswerException, BadAnswerException, ControlSummException {
        int[] answer = null;
        // Считываем первый байт (при корректном ответе должно быть 0x00)
        try {
            answer = this.serialPort.readIntArray(1, timeOut);
            if (answer[0] != 0) throw new BadAnswerException(answer[0]);
        } catch (SerialPortTimeoutException e) {
            throw new TimeOutAnswerException("Превышен интервал ожидания ответа (" + timeOut + " мс)");
        }
        // Считываем остальные байты
        try {
            answer = this.serialPort.readIntArray(byteCount - 1, timeOut);
            if (!(checkControlSumm(answer))) {
                throw new ControlSummException("Ошибка контрольной суммы.");
            }
        } catch (SerialPortTimeoutException e) {
            throw new TimeOutAnswerException("Превышен интервал ожидания ответа (" + timeOut + " мс)");
        }
        return answer;
    }

    /**
     * Проверка связи с уровнемером "Струна". Протокол обмена "Кедр" вер.1.4
     * В ответе ожидается 2 байта. 1-й = 0x00, 2-й - 0x55
     * @param delay - максимальное время ожидания ответа (мс)
     * @return
     * @throws SerialPortException
     */
    public boolean checkConnection(boolean showToScreen, int maxDelay) throws SerialPortException, StrunaAnswerException {
        boolean result = false;
        sendCommandToPort(RequestCommand.connection.getCommand());
        int[] answer;
        try {
            answer = getDataFromPort(2, maxDelay);
        } catch (TimeOutAnswerException e) {
            System.out.println(e.getMessage());
            return false;
        }
        if (answer[0] == 0x55) result = true;
        if (showToScreen) System.out.println("Проверка связи: " + result);
        return result;
    }

    /**
     * Получение конфигурации по всем каналам.
     * В ответе ожидается 18 байт.
     * @param showToScreen - выводить ли данные на экран
     * @param delay - максимальное время ожидания ответа (мс)
     * @return массив каналов, с полученной конфигурацией
     * @throws SerialPortException
     * @throws BadAnswerException
     * @throws TimeOutAnswerException
     */
    public ChannelConfiguration[] getChannelConfiguration(boolean showToScreen, int maxDelay) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.configuration.getCommand());
        int[] answer = getDataFromPort(18, maxDelay);
        ChannelConfiguration[] result = new ChannelConfiguration[16];
        for (int i = 0; i < result.length; i++) {
            result[i] = new ChannelConfiguration(i, answer[i]);
        }
        if (showToScreen) {
            System.out.println("Получены следующие конфигурационные данные:");
            for (int i = 0; i < result.length; i++) {
                System.out.println("Канал №" + i + " = " + answer[i]);
            }
        }
        return result;
    }

    /**
     * Получение и расшифровка конфигурации по выбранному каналу
     * @param showToScreen выводить ли данные на экран
     * @param maxDelay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return - массив логических данных по каналу
     * @throws TimeOutAnswerException
     * @throws SerialPortException
     * @throws BadAnswerException
     */
    public ChannelConfiguration getChannelConfiguration(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        // https://javarush.com/groups/posts/1925-pobitovihe-operacii
        ChannelConfiguration channelConfiguration = getChannelConfiguration(false,maxDelay)[channelNumber];
        boolean[] result = new boolean[8];
        for (int i = 0; i < result.length; i++) {
            result[i] = BitUtils.getBitBooleanValue(channelConfiguration.getChannelConfiguration(), i);
        }
        if (showToScreen) System.out.println(channelConfiguration);
        return channelConfiguration;
    }

    public boolean getState(boolean showToScreen, int maxDelay) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.state.getCommand());
        int[] answer = getDataFromPort(2, maxDelay);
        boolean result = false;
        if (BitUtils.getBitBooleanValue(answer[0],7))
                result = true;
           else result = false;
        if (showToScreen) {
          if (result) System.out.println("Состояние: БВ готов ");
          else  System.out.println("Состояние: БВ не готов ");
        }

        return result;
    }

    public static boolean checkControlSumm(int[] answer) {
        if (answer != null && answer.length <3) return true;
        int controlSumm = 0;
        // System.out.println("Считаем контрольную сумму");
        for (int i = 0; i < answer.length-1; i++) {
        //    System.out.println(Integer.toHexString(answer[i]));
            controlSumm = controlSumm ^ answer[i];
        }
        if (controlSumm == answer[answer.length-1]) return true;
        return false;
    }

    /**
     * Получение значения уровня по выбранному каналу
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение уровня
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public double getLevel(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.level.getCommand() + channelNumber);
        int[] answer = getDataFromPort(5, maxDelay);
        double result = answerParser(answer);
        if (showToScreen) System.out.println("Канал №" + channelNumber + ". Значение уровня = " + result + "мм.");
        return result;
    }

    /**
     * Получение значения плотности по выбранному каналу
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение плотности
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public double getDensity(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.density.getCommand() + channelNumber);
        int[] answer = getDataFromPort(5, maxDelay);
        double result = answerParser(answer);
        if (showToScreen) System.out.println("Канал №" + channelNumber + ". Значение плотности = " + result + "кг/м3");
        return result;
    }

    /**
     * Получение значения объёма по выбранному каналу
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение объёма
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public double getVolume(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.volume.getCommand() + channelNumber);
        int[] answer = getDataFromPort(5, maxDelay);
        double result = answerParser(answer);
        if (showToScreen) System.out.println("Канал №" + channelNumber + ". Значение объёма = " + result + "л.");
        return result;
    }

    /**
     * Получение значения массы по выбранному каналу
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение объёма
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public double getMass(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.mass.getCommand() + channelNumber);
        int[] answer = getDataFromPort(5, maxDelay);
        double result = answerParser(answer);
        if (showToScreen) System.out.println("Канал №" + channelNumber + ". Значение массы = " + result + "кг.");
        return result;
    }

    /**
     * Получение значения массы по выбранному каналу.
     * Единица измерения – 0.5 °С. Первый байт содержит значение нижнего датчика. Четвертый
     * байт содержит среднюю температуру продукта
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение объёма
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public double[] getTemp(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.temp.getCommand() + channelNumber);
        int[] answer = getDataFromPort(6, maxDelay);
        double[] result = new double[4];
        result[0] = answerParserTemp(answer[0]);
        result[1] = answerParserTemp(answer[1]);
        result[2] = answerParserTemp(answer[2]);
        result[3] = answerParserTemp(answer[3]);
        if (showToScreen) {
            System.out.println("Канал №" + channelNumber);
            System.out.println("\tЗначение температуры (датчик №1)= " + result[0]);
            System.out.println("\tЗначение температуры (датчик №2)= " + result[1]);
            System.out.println("\tЗначение температуры (датчик №3)= " + result[2]);
            System.out.println("\tЗначение температуры (среднее)= " + result[3]);
        }
        return result;
    }

    /**
     * Получение значения уровня воды по выбранному каналу.
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение объёма
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public int getWaterLevel(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.waterLevel.getCommand() + channelNumber);
        int[] answer = getDataFromPort(2, maxDelay);
        int result = answer[0];
        if (showToScreen) {
            System.out.println("Канал №" + channelNumber + ". Значение уровня подтоварной воды = " + result + " мм.");
        }
        return result;
    }

    /**
     * Получение значения массы по выбранному каналу.
     * Единица измерения – 0.5 °С. Первый байт содержит значение нижнего датчика. Четвертый
     * байт содержит среднюю температуру продукта
     * @param showToScreen выводить ли данные на экран
     * @param delay максимальное время ожидания ответа (мс)
     * @param channelNumber номер канала, по которому будет получаться информация
     * @return Значение объёма
     * @throws SerialPortException
     * @throws StrunaAnswerException
     */
    public double getTempUp(boolean showToScreen, int maxDelay, int channelNumber) throws SerialPortException, StrunaAnswerException {
        sendCommandToPort(RequestCommand.tempUp.getCommand() + channelNumber);
        int[] answer = getDataFromPort(2, maxDelay);
        double result = answerParserTemp(answer[0]);
        if (showToScreen) {
            System.out.println("Канал №" + channelNumber + ". Значение температуры (верхний датчик)= " + result);
        }
        return result;
    }

    /**
     * В данных возвращается значение параметра (Уровень, плотность, объём, масса). Значение параметра передаётся
     * тремя байтами в виде комбинации двоичной целой части и двоично-десятичной дробной части.
     */
    public static double answerParser(int[] answer) {
        double result = -999999.9;
        String byte0;
        String byte1;
        // Добавляем незначащий нуль
        if (answer[0] <= 15) byte0 = "0" + Integer.toHexString(answer[0]);
            else byte0 = Integer.toHexString(answer[0]);
        if (answer[1] <= 15) byte1 = "0" + Integer.toHexString(answer[1]);
            else byte1 = Integer.toHexString(answer[1]);
        String byte2 = Integer.toHexString((answer[2] & 0b11110000) >> 4);
        // Получаем целую часть
        result = Integer.decode("0x" + byte2 + byte1 + byte0);
        // Получаем дробную часть
        result = result + ((answer[2] & 0b00001111) / 10.0);
        return result;
    }

    /**
     * В данных возвращается значение параметра (Уровень, плотность, объём, масса). Значение параметра передаётся
     * тремя байтами в виде комбинации двоичной целой части и двоично-десятичной дробной части.
     */
    public static double answerParserTemp(int answer) {
//        System.out.println(Integer.toHexString(answer) + " " + Integer.toBinaryString(answer) + " (answer >> 7) == 1    " + Integer.toBinaryString(answer >> 7));
//        System.out.println(Integer.toHexString(answer) + " " + Integer.toBinaryString(answer) + " (answer & 0b01111111)    " + Integer.toBinaryString(answer & 0b01111111));
        if ((answer >> 7) == 1)
             return (answer & 0b01111111) / -2.0;
        else return (answer & 0b01111111) / 2.0;
    }





}