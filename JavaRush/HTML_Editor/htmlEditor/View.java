package htmlEditor;

import htmlEditor.listeners.*;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();     // Панель с двумя вкладками
    private JTextPane htmlTextPane = new JTextPane();       // Компонент для визуального редактирования html
    private JEditorPane plainTextPane = new JEditorPane();  // Компонент для редактирования html в виде текста, он будет отображать код html (теги и их содержимое)

    private UndoManager undoManager = new UndoManager();

    private UndoListener undoListener = new UndoListener(undoManager);

    public void undo() {
       try {
           undoManager.undo();
       } catch (Exception ex) {
           ExceptionHandler.log(ex);
       }

    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception ex) {
            ExceptionHandler.log(ex);
        }
    }


    public View() {
        // http://cloud-notes.blogspot.com/2012/12/look-and-feel-java.html
        // Использование Look And Feel в приложениях на java
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            ExceptionHandler.log(e);
        } catch (InstantiationException e) {
            ExceptionHandler.log(e);
        } catch (IllegalAccessException e) {
            ExceptionHandler.log(e);
        } catch (UnsupportedLookAndFeelException e) {
            ExceptionHandler.log(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String commandStr = e.getActionCommand();
        switch (commandStr) {
            case "Новый": {
                controller.createNewDocument();
                break;
            }
            case "Открыть": {
                controller.openDocument();
                break;
            }
            case "Сохранить": {
                controller.saveDocument();
                break;
            }
            case "Сохранить как...": {
                controller.saveDocumentAs();
                break;
            }
            case "Выход": {
                exit();
                break;
            }
            case "О программе": {
                showAbout();
                break;
            }


        }

    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init(){
        initGui();
        FrameListener frameListener = new FrameListener(this); // Слушатель (подписчик) событий окна
        addWindowListener(frameListener);
        setVisible(true);
    }

    public void exit(){
        controller.exit();
    }

    /**
     * Инициализация меню
     */
    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);

        getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }

    /**
     * Инициализация панелей редактора
     */
    public void initEditor() {

        htmlTextPane.setContentType("text/html"); // устанавливаем тип контента

        JScrollPane jScrollPaneHtml = new JScrollPane(htmlTextPane); // создаём новый локальный компонент JScrollPane на базе htmlTextPane
        tabbedPane.addTab("HTML",jScrollPaneHtml);

        JScrollPane jScrollPaneText = new JScrollPane(plainTextPane); // создаём новый локальный компонент JScrollPane на базе plainTextPane
        tabbedPane.addTab("Текст",jScrollPaneText);

        tabbedPane.setPreferredSize(new Dimension(500, 800)); //  Установка предпочтительного размера панели tabbedPane

        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);


         // Добавлять по центру панели контента текущего фрейма нашу панель с вкладками.
         // Получить панель контента текущего фрейма можно с помощью метода getContentPane(), его реализация унаследовалась от JFrame.
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * инициализация графического интерфейса
     */
    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }

    public void selectedTabChanged() {
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        } else {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    /**
     * Возвращает true, если выбрана вкладка, отображающая html
     * @return
     */
    public boolean isHtmlTabSelected() {
        if (tabbedPane.getSelectedIndex() == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Выбираем html вкладку и сбрасываем все правки
     */
    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();

    }

    /**
     * Устанавливает документ в панель редактирования
     */
    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    /**
     * Показывает диалоговое окно с информацией о программе
     */
    public void showAbout() {
      /*  JOptionPane.showMessageDialog(JOptionPaneTest.this,
                "Использование изображения в окне сообщений",
                TITLE_message, JOptionPane.INFORMATION_MESSAGE,
                icon);*/

        // JOptionPane.showMessageDialog(this, JOptionPane.INFORMATION_MESSAGE, "О программе", "Задача HTML Editor\nВыполена Карповым В. П.");
        // JOptionPane.showMessageDialog(this, this, "ass", 1);
        JOptionPane.showMessageDialog(this,"HTML редактор.\nВерсия 1.0", "О программе", JOptionPane.INFORMATION_MESSAGE);
    }
}

