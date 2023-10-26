package answerexception;
import answer.Byte1Answer;

/**
 * Класс описывает ответ, который получен от уровнемера и не является ожидаемым.
 * Ответ может указывать на неисправность канала, некорректную команду и т.д.
 */
public class BadAnswerException extends StrunaAnswerException{
    private Byte1Answer errorCode;

    public BadAnswerException(int errorCode) {
        super(Byte1Answer.getAnswerName(errorCode));
//        switch (errorCode) {
//           case  0x04: this.errorCode = answer.Byte1Answer.defectChannelOrCommand;
//           case  0x06: this.errorCode = answer.Byte1Answer.connectionError;
//           case  0x0C: this.errorCode = answer.Byte1Answer.unknownCommand;
//           case  0xFE: this.errorCode = answer.Byte1Answer.initialization;
//           case  0xFF: this.errorCode = answer.Byte1Answer.missChannelOrCommand;
//           default: this.errorCode = Byte1Answer.unknownError;
//         }
    }

    public Byte1Answer getErrorCode() {
        return errorCode;
    }
}
