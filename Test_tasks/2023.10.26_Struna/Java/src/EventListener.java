import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import struna14protocol.Struna14Protocol;

public class EventListener implements SerialPortEventListener {
    private Struna14Protocol struna14Protocol;

    public EventListener(Struna14Protocol struna14Protocol) {
        this.struna14Protocol = struna14Protocol;
    }

    @Override
    public void serialEvent (SerialPortEvent event) {
        if (event.isRXCHAR () && event.getEventValue () > 0){ /*Если происходит событие установленной маски и количество байтов в буфере более 0*/
            try {
                //String data2 = struna14Protocol.serialPort.readHexString(event.getEventValue ());
                byte[] answer = struna14Protocol.getSerialPort().readBytes();
                if ((answer[0] == 0) && (answer[1] == 85)) {
                    System.out.println("Длина массива = " + answer.length);
                    System.out.println("Связь с уровнемером установлена");
                } else {
                    System.out.println("Длина массива = " + answer.length);
                    System.out.println("Ошибка связи");
                }
            }
            catch (SerialPortException ex) {
                System.out.println("Прерывание");
                System.out.println (ex);
            }
        }
    }
}
