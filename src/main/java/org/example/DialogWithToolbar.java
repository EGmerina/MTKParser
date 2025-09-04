package org.example;

import javax.swing.*;
import java.awt.*;


public class DialogWithToolbar extends JFrame {

    public DialogWithToolbar() {
        // Настройка основного окна
        setTitle("Simple parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Создание текстовой области для содержимого
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Настройка layout и добавление компонентов
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Создание меню (опционально)
        createMenuBar();
    }


    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Меню "Файл"
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        // Меню "Правка"
        JMenu editMenu = new JMenu("Text");
        JMenuItem languageItem = new JMenuItem("Language");
        JMenuItem grammaticItem = new JMenuItem("Grammatic");
        JMenuItem classificationItem = new JMenuItem("Classification");
        JMenuItem analysisItem = new JMenuItem("Analysis");
        JMenuItem diagnosticsItem = new JMenuItem("Diagnostics");
        JMenuItem testingItem = new JMenuItem("Testing");

        editMenu.add(languageItem);
        editMenu.add(grammaticItem);
        editMenu.add(classificationItem);
        editMenu.add(analysisItem);
        editMenu.add(diagnosticsItem);
        editMenu.add(testingItem);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        // Запуск в Event Dispatch Thread для безопасности
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Установка системного look and feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DialogWithToolbar dialog = new DialogWithToolbar();
                dialog.setVisible(true);
            }
        });
    }
}
