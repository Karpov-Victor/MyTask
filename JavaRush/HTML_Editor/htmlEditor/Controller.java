package htmlEditor;

import htmlEditor.listeners.*;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
        // controller.exit();
    }

    public void init(){
        createNewDocument();
    }

    /**
     * Выход из программы (System.exit(0))
     */
    public void exit(){
        System.exit(0);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    /**
     * Сбрасывает текущий документ. Удаляет правки. Создаёт новый документ
     */
    public void resetDocument() {
        UndoListener undoListener = view.getUndoListener();
        if (document != null) document.removeUndoableEditListener(undoListener);
        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(undoListener);
        view.update();
    }

    /**
     * Записывает переданный текст с html тегами в документ document
     * @param text
     */
    public void setPlainText(String text) {
        resetDocument();
        StringReader stringReader = new StringReader(text);
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        try {
            htmlEditorKit.read(stringReader,document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    /**
     * получает текст из документа со всеми html тегами.
     * @return
     */
    public String getPlainText() {
        StringWriter stringWriter = new StringWriter();
        String result = null;
        try {
            new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());
            result = stringWriter.toString();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return result;
    }

    /**
     * Метод создания нового документа
     */
    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        currentFile = null;
    }
    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        int result = jFileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            resetDocument();
            currentFile = jFileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try (FileReader fileReader = new FileReader(currentFile)) {
                new HTMLEditorKit().read(fileReader,document,0);
                view.resetUndo();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }

    }

    /**
     * Метод для сохранения файла под новым именем
     */
    public void saveDocumentAs() {
        view.selectHtmlTab();                               // Переключаем представление на html вкладку.
        JFileChooser jFileChooser = new JFileChooser();     // Создаём новый объект для выбора файла
        jFileChooser.setFileFilter(new HTMLFileFilter());   // Устанавливаем фильтр
        int result = jFileChooser.showSaveDialog(view);
        // Если файл выбран, то представим его в сообщении
        if (result == JFileChooser.APPROVE_OPTION ) {
            currentFile = jFileChooser.getSelectedFile();   // установка заголовка окна
            view.setTitle(currentFile.getName());
            try (FileWriter fileWriter = new FileWriter(currentFile)) {
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }

    /**
     * Сохранение уже открытого файла
     */
    public void saveDocument() {
        view.selectHtmlTab();
        if (currentFile == null) {
            saveDocumentAs();
        } else {
            view.setTitle(currentFile.getName());
            try (FileWriter fileWriter = new FileWriter(currentFile)) {
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }


}
