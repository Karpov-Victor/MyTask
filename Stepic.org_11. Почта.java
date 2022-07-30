/*
��� ������� ��������� ���������� �� ��������� ���������� ���� ������� � ���������� ����������� � �������� ������-��������������� ��� � ��� ���� ��������� ������ ��� � ���������� � �����������.
��� ����� �������, ����������� ������ �������������� �������� �������.
��� ������ ���������� ���, ����������� ��� ������������ ��������.
���������: ��������, ������� ����� ��������� �� �����.
� ����� �������� ����� �������� �� ���� � ���� ������������ ������.


public static interface Sendable {
    String getFrom();
    String getTo();
}
� Sendable ���� ��� ����������, ������������ ��������� ����������� �������:


// ����������� �����,������� ��������� �������������� ������ ��������
// ��������� � ���������� ������ � ��������������� ����� ������.
public static abstract class AbstractSendable implements Sendable {

    protected final String from;
    protected final String to;

    public AbstractSendable(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractSendable that = (AbstractSendable) o;

        if (!from.equals(that.from)) return false;
        if (!to.equals(that.to)) return false;

        return true;
    }

}
������ ����� ��������� ������� ������, � ������� ��������� ������ ��������� ���������.


// ������, � �������� ���� �����, ������� ����� �������� � ������� ������ `getMessage`
public static class MailMessage extends AbstractSendable {

    private final String message;

    public MailMessage(String from, String to, String message) {
        super(from, to);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MailMessage that = (MailMessage) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

}

������ ����� ��������� �������� �������:


// �������, ���������� ������� ����� �������� � ������� ������ `getContent`
public static class MailPackage extends AbstractSendable {
    private final Package content;

    public MailPackage(String from, String to, Package content) {
        super(from, to);
        this.content = content;
    }

    public Package getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MailPackage that = (MailPackage) o;

        if (!content.equals(that.content)) return false;

        return true;
    }

}

// ��� ���� ���� ������� ����������� ��������� �������:
// �����, ������� ������ �������. � ������� ���� ��������� �������� ����������� � ������������� ��������.
public static class Package {
    private final String content;
    private final int price;

    public Package(String content, int price) {
        this.content = content;
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Package aPackage = (Package) o;

        if (price != aPackage.price) return false;
        if (!content.equals(aPackage.content)) return false;

        return true;
    }
}
������ ���������� ������, ������� ���������� ������ ��������� �������:


// ���������, ������� ������ �����, ������� ����� �����-���� ������� ���������� �������� ������.
public static interface MailService {
    Sendable processMail(Sendable mail);
}


// �����, � ������� ������ ������ ��������� �����
public static class RealMailService implements MailService {

    @Override
    public Sendable processMail(Sendable mail) {
        // ����� ������ ��� ��������� ������� �������� �����.
        return mail;
    }
}
��� ���������� ������� ����� �������, ������ �� ������� �������� MailService:

1) UntrustworthyMailWorker � �����, ������������ ����������� ��������� �����, ������� ������ ����, ����� �������� �������� ������ ��������������� � ������ �����, ��������������� �������� ���� ������ ������ ������� ���, � �����, � ����� ������, �������� ������������ ������ ��������������� ���������� RealMailService. � UntrustworthyMailWorker ������ ���� ����������� �� ������� MailService ( ��������� ������ processMail ������� �������� ������� ���������� �� ���� processMail ������� ��������, � �. �.) � ����� getRealMailService, ������� ���������� ������ �� ���������� ��������� RealMailService.

2) Spy � �����, ������� ��������� � ���� �������� ���������, ������� �������� ����� ��� ����. ������ �������������� �� ���������� Logger, � ������� �������� ����� ����� �������� � ���� ���������. �� ������ ������ �� ��������� ������ MailMessage � ����� � ������ ��������� ��������� (� ���������� ����� �������� ����� � �������� ������� �� �������� ����� �����):
2.1) ���� � �������� ����������� ��� ���������� ������ "Austin Powers", �� ����� �������� � ��� ��������� � ������� WARN: Detected target mail correspondence: from {from} to {to} "{message}"
2.2) �����, ���������� �������� � ��� ��������� � ������� INFO: Usual correspondence: from {from} to {to}

3) Thief � ���, ������� ������ ����� ������ ������� � ���������� ��� ���������. ��� ��������� � ������������ ���������� int � ����������� ��������� �������, ������� �� ����� ��������. �����, � ������ ������ ������ �������������� ����� getStolenValue, ������� ���������� ��������� ��������� ���� �������, ������� �� ��������. ��������� ���������� ��������� �������: ������ �������, ������� ������ ����, �� ������ �����, ����� ��, ������ � ������� ��������� � ���������� ������� "stones instead of {content}".

4) Inspector � ���������, ������� ������ �� ������������ � ����������� ��������� � ���� ������� � ���� ����������, ���� ���� ���������� �������� �������. ���� �� ������� ����������� ������� � ����� �� ����������� ���������� ("weapons" � "banned substance"), �� �� ������� IllegalPackageException. ���� �� ������� �������, ��������� �� ������ (�������� ����� "stones"), �� ������� ��������� � ���� StolenPackageException. ��� ���������� �� ������ �������� �������������� � ���� ������������� ����������.

��� ������ ������ ���� ���������� ��� ��������� � �����������, ��� ��� � �������� �������� ��� ��� ����� ���������� �� ������� �����, ������� ���������� ������������� � ��������� ���������. ��� �������� �� ������� ������ ��������� ��������� ������� �������� � ������������� ��� ���������� ������ java.util.logging. ��� �����������, �������� ��� ������� �������� Sendable ������ �������������� ���������� instanceof.

public static final String AUSTIN_POWERS = "Austin Powers";
public static final String WEAPONS = "weapons";
public static final String BANNED_SUBSTANCE = "banned substance";

// implement UntrustworthyMailWorker, Spy, Inspector, Thief, StolenPackageException, IllegalPackageException as public static classes here

*/

// ====================== ������ ===================
package com;
import java.util.logging.*;

public class Main {

    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";

    public static void main(String[] args) {
        // write your code here
        Sendable mailMessage  = new MailMessage("Austin Powers", "����", "������");
        Sendable mailPackage = new MailPackage("��������", "����", new Package("��������� �� �������", 100));


        Thief thief = new Thief(90);

        // mailPackage = thief.processMail(mailPackage);

        Inspector inspector = new Inspector();
        // inspector.processMail(mailPackage);

        Logger myLogger = Logger.getLogger(Main.class.getName());
        Spy spy = new Spy(myLogger);
        // mailMessage = spy.processMail(mailMessage);



        MailService[] mailArray = {thief, spy, inspector};
        UntrustworthyMailWorker untrustworthyMailWorker = new UntrustworthyMailWorker(mailArray);
        untrustworthyMailWorker.processMail(mailMessage);


    }

    /*
    ���������: ��������, ������� ����� ��������� �� �����.
    � ����� �������� ����� �������� �� ���� � ���� ������������ ������.
    */
    public static interface Sendable {
        String getFrom();
        String getTo();
    }

    /*
    ����������� �����,������� ��������� �������������� ������ ��������
    ��������� � ���������� ������ � ��������������� ����� ������.
    */
    public static abstract class AbstractSendable implements Sendable {

        protected final String from;
        protected final String to;

        public AbstractSendable(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public String getTo() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AbstractSendable that = (AbstractSendable) o;

            if (!from.equals(that.from)) return false;
            if (!to.equals(that.to)) return false;

            return true;
        }

    }

    /*
    ������, � �������� ���� �����, ������� ����� �������� � ������� ������ `getMessage`
    */
    public static class MailMessage extends AbstractSendable {

        private final String message;

        public MailMessage(String from, String to, String message) {
            super(from, to);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailMessage that = (MailMessage) o;

            if (message != null ? !message.equals(that.message) : that.message != null) return false;

            return true;
        }

    }

    /*
    �������, ���������� ������� ����� �������� � ������� ������ `getContent`
    */
    public static class MailPackage extends AbstractSendable {
        private final Package content;

        public MailPackage(String from, String to, Package content) {
            super(from, to);
            this.content = content;
        }

        public Package getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailPackage that = (MailPackage) o;

            if (!content.equals(that.content)) return false;

            return true;
        }

    }

    /*
    �����, ������� ������ �������. � ������� ���� ��������� �������� ����������� � ������������� ��������.
    */
    public static class Package {
        private final String content;
        private final int price;

        public Package(String content, int price) {
            this.content = content;
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Package aPackage = (Package) o;

            if (price != aPackage.price) return false;
            if (!content.equals(aPackage.content)) return false;

            return true;
        }
    }

    /*
    ���������, ������� ������ �����, ������� ����� �����-���� ������� ���������� �������� ������.
    */
    public static interface MailService {
        Sendable processMail(Sendable mail);
    }

    /*
    �����, � ������� ������ ������ ��������� �����
    */
    public static class RealMailService implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            // ����� ������ ��� ��������� ������� �������� �����.
            return mail;
        }
    }

    /*
    1) UntrustworthyMailWorker � �����, ������������ ����������� ��������� �����,
    ������� ������ ����, ����� �������� �������� ������ ��������������� � ������
    �����, ��������������� �������� ���� ������ ������ ������� ���, � �����, �
    ����� ������, �������� ������������ ������ ��������������� ����������
    RealMailService. � UntrustworthyMailWorker ������ ���� ����������� �� �������
    MailService ( ��������� ������ processMail ������� �������� ������� ����������
    �� ���� processMail ������� ��������, � �. �.) � ����� getRealMailService,
    ������� ���������� ������ �� ���������� ��������� RealMailService.
     */
    public static class  UntrustworthyMailWorker implements MailService {
        private MailService[] mailArray;
        private Sendable finishMail;

        public UntrustworthyMailWorker(MailService[] mailArray) {
            this.mailArray = mailArray;
        }

        @Override
        public Sendable processMail(Sendable mail) {

            for (int i = 0; i < mailArray.length; i++) {
                mail = mailArray[i].processMail(mail);
            }
            RealMailService finishMailService = new RealMailService();
            finishMail = mail;
            return finishMailService.processMail(mail);
        }

        public Sendable getRealMailService() {
            return finishMail;
        }
    }

    public static class Thief implements MailService {
        private int minCost;
        public Thief(int minCost) {
            this.minCost = minCost;
        }
        private int StolenValue;

        public int getStolenValue() {
            return StolenValue;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            // ��������� ��� �� ��������, ���� ������ ���������� ��� ������
            if (mail instanceof MailMessage) {
                return mail;
            } else {
                MailPackage mailPackage = (MailPackage) mail;
                int mailPrice = mailPackage.getContent().getPrice();
                if (mailPrice >= minCost) { // ������ �������, ���� ���� ������
                    StolenValue = StolenValue + mailPrice;
                    return new MailPackage(mailPackage.getTo(), mailPackage.getFrom(), new Package("stones instead of" + mailPackage.getContent().getContent(), 0));
                }
                return mail;
            }
        }
    }

    public static class Spy implements MailService {
        private Logger spyLogger; // = Logger.getLogger("SpyLogger");

        public Spy(Logger spyLogger) {
            this.spyLogger = spyLogger;
        }

        @Override
        public Sendable processMail(Sendable mail) {

            if (mail instanceof MailPackage) {
                return mail;
            } else {
                MailMessage mailMessage = (MailMessage) mail;
                if (mailMessage.getFrom().contains(AUSTIN_POWERS) || mailMessage.getTo().contains(AUSTIN_POWERS)) {
                    spyLogger.warning("Detected target mail correspondence: from " + mailMessage.getFrom()
                            +" to " + mailMessage.getTo() + " \"" + mailMessage.getMessage() + " \"") ;
                } else {
                    spyLogger.info("Usual correspondence: from " + mailMessage.getFrom()
                            +" to " + mailMessage.getTo()); ;
                }
            }
            return mail;
        }

    }

    public static class  Inspector implements MailService {
        @Override
        public Sendable processMail(Sendable mail) {
            if (mail instanceof MailMessage) {
                return mail;
            } else {
                MailPackage mailPackage = (MailPackage) mail;
                String mailContent = mailPackage.getContent().getContent();
                if (mailContent.contains(WEAPONS) || mailContent.contains(BANNED_SUBSTANCE)) {
                    throw new IllegalPackageException();
                }
                if (mailContent.contains("stones")) {
                    throw new StolenPackageException ();
                }
                return mail;
            }
        }
    }

    public static class StolenPackageException extends RuntimeException {
        /*
        public StolenPackageException(String message) {
            super(message);
        }
        */

    }

    public static class IllegalPackageException extends RuntimeException {
        /*
        public IllegalPackageException() {
            super();
        }
        */
    }
}
