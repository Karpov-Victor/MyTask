package gui;

import answerexception.StrunaAnswerException;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import struna14protocol.RequestCommand;
import struna14protocol.Struna14Protocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class MainForm {
    private static final Struna14Protocol struna14 = new Struna14Protocol();
    private static final MainForm MAIN_FORM = new MainForm();
    private static final JFrame frame = new JFrame("Struna ver. 1.4");       // создаем объект JFrame с заголовком
    private static final JPanel choicePortPanel = new JPanel();
    private static final JButton btnOpenPort = new JButton("Открыть порт");
    private static final JComboBox<String> comboBoxPort = comboBoxGetPortsList();
    private static final JPanel sendCommandPanel = new JPanel();
    private static final JButton btnSendCommand = new JButton("Отправить команду");
    private static final JComboBox<String> comboBoxCommand = new JComboBox<>();
    private static final JComboBox<String> comboBoxChannel = new JComboBox<>();
    private static final JTextArea textArea = new JTextArea("",30,5);
    private static final JScrollPane scrollPaneTextArea = new JScrollPane(textArea);
    private static final JButton btnGetChannelAllData = new JButton("Получить все данные по каналу");
    private static final JButton btnGetAllChannelsAllData = new JButton("Получить все данные по всем каналам");
    private static final JCheckBox checkBoxCycle = new JCheckBox("Получать циклично", false);
    private static final JTextField textField = new JTextField();
    private static Thread cycleThread;
    private static final int generalDelay = 1000;
    private static final int defaultCycleTime = 5000;

    private static final Runnable GET_DATA_CYCLE_TASK = new Runnable() {
        public void run() {
            int cycleTime;
            try {
                cycleTime = getCycleTime();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Требуется ввести целое число в поле интервала опроса.\nУстановлено значение по умолчанию.");
                textField.setText(String.valueOf(defaultCycleTime));
                checkBoxCycle.setSelected(false);
                return;
            }

            while (checkBoxCycle.isSelected()) {
                textArea.append("=======================================\n");
                getAllChannelsAllData(false, generalDelay);
                textArea.setCaretPosition(textArea.getDocument().getLength()); // Установка курсора в конец документа
                try {
//                    System.out.println("Поток THREAD_CYCLE засыпает");
                    cycleThread.sleep(cycleTime);
                } catch (InterruptedException ex) {
//                    System.out.println("Поток THREAD_CYCLE прерывается");
                    checkBoxCycle.setSelected(false);
                }
            }
        }
    };;

    private static final int getCycleTime() {
        int cycleTime = Integer.parseInt(textField.getText());
        return cycleTime;
    };

    private static void componentsOnOff(boolean setOn) {
        btnSendCommand.setEnabled(setOn);
        btnGetChannelAllData.setEnabled(setOn);
        btnGetAllChannelsAllData.setEnabled(setOn);
        textField.setEnabled(setOn);
    }

    //public static void main(String[] args) {
    public static void createMainForm() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(570, 700);                    // Устанавливаем размеры окна
        frame.setLocationRelativeTo(null);                      // окно - в центре экрана
        frame.setResizable(false);                              // Запрещаем изменение размеров окна


        Container contentPane = frame.getContentPane();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        contentPane.add(comboBoxPort); // Выпадающее меню выбора порта
        layout.putConstraint(SpringLayout.EAST , comboBoxPort, 100, SpringLayout.WEST , comboBoxPort); // Ширина компонента

        contentPane.add(btnOpenPort); // Кнопка открытия порта
        layout.putConstraint(SpringLayout.EAST , btnOpenPort, 150, SpringLayout.WEST , btnOpenPort); // Ширина компонента
        btnOpenPort.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openPortFormAction();
                }
            });

        contentPane.add(btnGetChannelAllData); // Кнопка получить все данные по каналу
        btnGetChannelAllData.setEnabled(false);
        layout.putConstraint(SpringLayout.EAST , btnGetChannelAllData, 260, SpringLayout.WEST , btnGetChannelAllData); // Ширина компонента
        btnGetChannelAllData.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {getChannelAllData(false,1000, comboBoxChannel.getSelectedIndex());}
            });

        contentPane.add(comboBoxCommand); // Выпадающее меню выбора команды
        layout.putConstraint(SpringLayout.EAST , comboBoxCommand, 260, SpringLayout.WEST , comboBoxPort); // Ширина компонента
        addCommandsToComboBox(comboBoxCommand);

        contentPane.add(comboBoxChannel); // Выпадающее меню выбора канала
        layout.putConstraint(SpringLayout.EAST , comboBoxChannel, 100, SpringLayout.WEST , comboBoxChannel); // Ширина компонента
        for (int i = 0; i < 15; i++) {
            comboBoxChannel.addItem("Канал № " + String.valueOf(i));
        }

        contentPane.add(btnSendCommand); // Кнопка отправки команды
        btnSendCommand.setEnabled(false);
        layout.putConstraint(SpringLayout.EAST , btnSendCommand, 150, SpringLayout.WEST , btnSendCommand); // Ширина компонента
        btnSendCommand.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {exchangeData(false, generalDelay);
                }
            });

        contentPane.add(scrollPaneTextArea); // Текстовое поле
        scrollPaneTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        layout.putConstraint(SpringLayout.EAST , scrollPaneTextArea, 530, SpringLayout.WEST , scrollPaneTextArea); // Ширина компонента
        scrollPaneTextArea.setAutoscrolls(true);
        textArea.setEditable(false);

        contentPane.add(scrollPaneTextArea); // Текстовое поле
        scrollPaneTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        layout.putConstraint(SpringLayout.EAST , scrollPaneTextArea, 530, SpringLayout.WEST , scrollPaneTextArea); // Ширина компонента
        textArea.setEditable(false);

        contentPane.add(btnGetAllChannelsAllData); // Кнопка получить все данные по каналу
        btnGetAllChannelsAllData.setEnabled(false);
        layout.putConstraint(SpringLayout.EAST , btnGetAllChannelsAllData, 280, SpringLayout.WEST , btnGetAllChannelsAllData); // Ширина компонента
        btnGetAllChannelsAllData.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {getAllChannelsAllData(false, 1000);
                    textArea.append("=================================================================\n");}
            });



        contentPane.add(checkBoxCycle); // Кнопка получить все данные по каналу
        checkBoxCycle.setEnabled(false);
        // layout.putConstraint(SpringLayout.EAST , btnGetAllChannelsAllData, 280, SpringLayout.WEST , btnGetAllChannelsAllData); // Ширина компонента
        checkBoxCycle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    cycleThread = new Thread(GET_DATA_CYCLE_TASK);
                    cycleThread.setDaemon(true);
                    cycleThread.start();
                    componentsOnOff(false);
                } else {
                    checkBoxCycle.setSelected(false);
                    componentsOnOff(true);
                    cycleThread.interrupt();
                };
            }
        });

        layout.putConstraint(SpringLayout.WEST , comboBoxPort, 10, SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.NORTH, comboBoxPort, 10, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, btnOpenPort, 10,   SpringLayout.EAST, comboBoxPort);
        layout.putConstraint(SpringLayout.NORTH, btnOpenPort, 0,   SpringLayout.NORTH, comboBoxPort);
        layout.putConstraint(SpringLayout.WEST, btnGetChannelAllData, 10,   SpringLayout.EAST, btnOpenPort);
        layout.putConstraint(SpringLayout.NORTH, btnGetChannelAllData, 0,   SpringLayout.NORTH, btnOpenPort);

        layout.putConstraint(SpringLayout.WEST, comboBoxCommand, 10, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, comboBoxCommand, 10, SpringLayout.SOUTH, comboBoxPort);
        layout.putConstraint(SpringLayout.NORTH, comboBoxChannel, 0,   SpringLayout.NORTH, comboBoxCommand);
        layout.putConstraint(SpringLayout.WEST, comboBoxChannel, 10,   SpringLayout.EAST, comboBoxCommand);
        layout.putConstraint(SpringLayout.NORTH, btnSendCommand, 0,   SpringLayout.NORTH, comboBoxChannel);
        layout.putConstraint(SpringLayout.WEST, btnSendCommand, 10,   SpringLayout.EAST, comboBoxChannel);

        layout.putConstraint(SpringLayout.WEST, scrollPaneTextArea, 0, SpringLayout.WEST, comboBoxCommand);
        layout.putConstraint(SpringLayout.NORTH, scrollPaneTextArea, 10, SpringLayout.SOUTH, comboBoxCommand);
        layout.putConstraint(SpringLayout.WEST, btnGetAllChannelsAllData, 0, SpringLayout.WEST, scrollPaneTextArea);
        layout.putConstraint(SpringLayout.NORTH, btnGetAllChannelsAllData, 10, SpringLayout.SOUTH, scrollPaneTextArea);

        layout.putConstraint(SpringLayout.NORTH, checkBoxCycle, 0,   SpringLayout.NORTH, btnGetAllChannelsAllData);
        layout.putConstraint(SpringLayout.WEST, checkBoxCycle, 10,   SpringLayout.EAST, btnGetAllChannelsAllData);

        contentPane.add(textField);

        layout.putConstraint(SpringLayout.NORTH, textField, 0,   SpringLayout.NORTH, checkBoxCycle);
        layout.putConstraint(SpringLayout.WEST, textField, 10,   SpringLayout.EAST, checkBoxCycle);

        frame.setVisible(true);

        layout.putConstraint(SpringLayout.EAST , textField, 90, SpringLayout.WEST , textField); // Ширина компонента
        layout.putConstraint(SpringLayout.SOUTH , textField, btnGetAllChannelsAllData.getHeight(), SpringLayout.NORTH , textField); // высота компонента
        textField.setEnabled(false);
        textField.setText(String.valueOf(defaultCycleTime));

    }

    public static JComboBox<String> comboBoxGetPortsList() {
        JComboBox<String> comboBoxPort = new JComboBox<>(SerialPortList.getPortNames());
        return comboBoxPort;
    }

    public static void openPortFormAction() {
        String comPortName = comboBoxPort.getItemAt(comboBoxPort.getSelectedIndex());
        if (btnOpenPort.getText().equals("Открыть порт")) {
            MAIN_FORM.struna14.setSerialPort(new SerialPort(comPortName));
            if (MAIN_FORM.struna14.openPort()) {
                btnOpenPort.setText("Закрыть порт");
                comboBoxPort.setEnabled(false);
                btnSendCommand.setEnabled(true);
                btnGetChannelAllData.setEnabled(true);
                btnGetAllChannelsAllData.setEnabled(true);
                checkBoxCycle.setEnabled(true);
                textField.setEnabled(true);
                System.out.println(comPortName + " успешно открыт");
            }
            else {
                System.out.println(comPortName + " не открыт");
                JOptionPane.showMessageDialog(frame, "Ошибка открытия порта " + comPortName);
            }

        } else {
            if (MAIN_FORM.struna14.closePort()) {
                btnOpenPort.setText("Открыть порт");
                checkBoxCycle.setEnabled(false);
                checkBoxCycle.setSelected(false);
                comboBoxPort.setEnabled(true);
                btnSendCommand.setEnabled(false);
                btnGetChannelAllData.setEnabled(false);
                btnGetAllChannelsAllData.setEnabled(false);
                textField.setEnabled(false);
                System.out.println(MAIN_FORM.struna14.getSerialPort().getPortName() + " успешно закрыт");
            }
            else System.out.println(comPortName + " не закрыт");
        }
    }

    public static void addCommandsToComboBox(JComboBox<String> targetComboBox) {
        for (RequestCommand command : RequestCommand.values()) {
            targetComboBox.addItem(command.getCommandName());
        }
    }

    /**
     * получить все данные по всем каналам и вывести в textArea
     * @param showToScreen
     * @param maxDelay
     */
    public static void getAllChannelsAllData(boolean showToScreen, int maxDelay) {
        for (int i = 0; i < 15; i++) {
            getChannelAllData(showToScreen, generalDelay, i);
        }

    }

    /**
     * Получить все данные по каналу и вывести их в textArea
     * @param showToScreen
     * @param paramDelay
     * @param channelNumber
     */
    public static void getChannelAllData(boolean showToScreen, int maxChannelDelay, int channelNumber) {
        int paramDelay = maxChannelDelay / 7;
        try {
            if ((MAIN_FORM.struna14.getChannelConfiguration(showToScreen, maxChannelDelay, channelNumber).getChannelConfiguration() >> 7) != 1) {
                textArea.append("Канал " + channelNumber + " не подключён\n");
                return;
            }
        } catch (SerialPortException | StrunaAnswerException e) {
            textArea.append("Опрос канала № " + channelNumber + ": " + e.getMessage() +"\n");
            return;
        }

        textArea.append("Канал № " + channelNumber + "\n");
        try {
                textArea.append("    Уровень: \t" + Double.toString(MAIN_FORM.struna14.getLevel(showToScreen, paramDelay, channelNumber)) + "\n");
            } catch (SerialPortException | StrunaAnswerException e) {
                textArea.append("    Уровень: \t" + e.getMessage() + "\n");
            }


        try {
                textArea.append("    Плотность: \t" + Double.toString(MAIN_FORM.struna14.getDensity(showToScreen, paramDelay, channelNumber)) + "\n");
            } catch (SerialPortException | StrunaAnswerException e) {
                textArea.append("    Плотность: \t" + e.getMessage() + "\n");
            }

            try {
                textArea.append("    Объём: \t" + Double.toString(MAIN_FORM.struna14.getVolume(showToScreen, paramDelay, channelNumber)) + "\n");
            } catch (SerialPortException | StrunaAnswerException e) {
                textArea.append("    Объём: \t" + e.getMessage() + "\n");
            }

            try {
                textArea.append("    Масса: \t" + Double.toString(MAIN_FORM.struna14.getMass(showToScreen, paramDelay, channelNumber)) + "\n");
            } catch (SerialPortException | StrunaAnswerException e) {
                textArea.append("    Масса: \t" + e.getMessage() + "\n");
            }

        try {
            textArea.append("    Темп. (средн): " + Double.toString(MAIN_FORM.struna14.getTemp(showToScreen, paramDelay, channelNumber)[0]) + "\n");
        } catch (SerialPortException | StrunaAnswerException e) {
            textArea.append("    Темп. (средн): " + e.getMessage() + "\n");
        }

        try {
            textArea.append("    Ур. подт. воды: " + Integer.toString(MAIN_FORM.struna14.getWaterLevel(showToScreen, paramDelay, channelNumber)) + "\n");
        } catch (SerialPortException | StrunaAnswerException e) {
            textArea.append("    Ур. подт. воды: " + e.getMessage() + "\n");
        }

        try {
            textArea.append("    Темп. верх. ДТ: " + Double.toString(MAIN_FORM.struna14.getTempUp(showToScreen, paramDelay, channelNumber)) + "\n");
        } catch (SerialPortException | StrunaAnswerException e) {
            textArea.append("    Темп. верх. ДТ: " + e.getMessage() + "\n");
        }
    }

    /**
     * Обмен данными по нажатию на кнопку.
     * Отправить команду, получить и расшифровать ответ.
     * Вывести ответ в TextArea
     * @param showToScreen
     * @param maxDelay
     */
    public static void exchangeData(boolean showToScreen, int maxDelay) {
        String commandName = comboBoxCommand.getItemAt(comboBoxCommand.getSelectedIndex());
        int channelNumber = comboBoxChannel.getSelectedIndex();
        String answer = "Неизвестная ошибка";
        try {
            switch (commandName){
                case "Проверить связь с БВ": {
                    if (MAIN_FORM.struna14.checkConnection(showToScreen, maxDelay)) {
                        answer = "выполнена успешно";
                    } else {
                        answer = "не выполнено";
                    }
                    textArea.append(commandName + ". " + answer + "\n");
                    return;
                }
                case "Получить конфигурацию": {
                    answer = MAIN_FORM.struna14.getChannelConfiguration(showToScreen, maxDelay, channelNumber).toString();
                    break;
                }
                case "Получить состояние": {
                    if (MAIN_FORM.struna14.getState(showToScreen, maxDelay)) {
                        answer = "БВ готов";
                    } else {
                        answer = "БВ не готов";
                    }
                    textArea.append(commandName + ". " + answer + "\n");
                    return;
                }
                case "Получить значение уровня": {
                    answer = Double.toString(MAIN_FORM.struna14.getLevel(showToScreen, maxDelay, channelNumber));
                    break;
                }
                case "Получить значение плотности": {
                    answer = Double.toString(MAIN_FORM.struna14.getDensity(showToScreen, maxDelay, channelNumber));
                    break;
                }
                case "Получить значение объёма": {
                    answer = Double.toString(MAIN_FORM.struna14.getVolume(showToScreen, maxDelay, channelNumber));
                    break;
                }
                case "Получить значение массы": {
                    answer = Double.toString(MAIN_FORM.struna14.getMass(showToScreen, maxDelay, channelNumber));
                    break;
                }
                case "Получить значение температуры": {
                    commandName = "Получить среднее значение температуры";
                    answer = Double.toString(MAIN_FORM.struna14.getTemp(showToScreen, maxDelay, channelNumber)[0]);
                    break;
                }
                case "Получить значение подтоварной воды": {
                    answer = Integer.toString(MAIN_FORM.struna14.getWaterLevel(showToScreen, maxDelay, channelNumber));
                    break;
                }
                case "Получить значение температуры верхнего ДТ": {
                    answer = Double.toString(MAIN_FORM.struna14.getTempUp(showToScreen, maxDelay, channelNumber));
                    break;
                }
            }

        } catch (StrunaAnswerException | SerialPortException e) {
            answer = e.getMessage();
        }
        textArea.append(commandName + " по каналу № " + channelNumber + " : " + answer + "\n");
    }


}
