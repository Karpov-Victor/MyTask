package answer;

public class ChannelConfiguration {
    private int channelNumber;
    private int channelConfiguration;
    String bit0 = "Измерение уровня продукта";
    String bit1 = "Измерение температуры продукта";
    String bit2 = "Наличие информации по объему";
    String bit3 = "-";
    String bit4 = "Измерение уровня подтоварной воды";
    String bit5 = "Измерение плотности продукта";
    String bit6 = "-";
    String bit7 = "Наличие канала";

    public ChannelConfiguration(int channelNumber, int channelConfiguration) {
        this.channelNumber = channelNumber;
        this.channelConfiguration = channelConfiguration;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public int getChannelConfiguration() {
        return channelConfiguration;
    }

    public String getBit0() {
        return bit0;
    }

    public String getBit1() {
        return bit1;
    }

    public String getBit2() {
        return bit2;
    }

    public String getBit3() {
        return bit3;
    }

    public String getBit4() {
        return bit4;
    }

    public String getBit5() {
        return bit5;
    }

    public String getBit6() {
        return bit6;
    }

    public String getBit7() {
        return bit7;
    }

    @Override
    public String toString() {
        return "Конфигурация канала №" + channelNumber + ": \n" +
                BitUtils.getBitIntValue(channelConfiguration, 0) + " = " + bit0 + "\n" +
                BitUtils.getBitIntValue(channelConfiguration, 1) + " = " + bit1 + "\n" +
                BitUtils.getBitIntValue(channelConfiguration, 2) + " = " + bit2 + "\n" +
                // "bit3" + " \t\t\t\t\t\t\t\t = " + BitUtils.getBitIntValue(channelConfiguration, 3) + "\n" +
                BitUtils.getBitIntValue(channelConfiguration, 4) + " = " + bit4 + "\n" +
                BitUtils.getBitIntValue(channelConfiguration, 5) + " = " + bit5 + "\n" +
                // "bit6" + " \t\t\t\t\t\t\t\t = " + BitUtils.getBitIntValue(channelConfiguration, 6) + "\n" +
                BitUtils.getBitIntValue(channelConfiguration, 7) + " = " + bit7 + "\n";
    }
}
