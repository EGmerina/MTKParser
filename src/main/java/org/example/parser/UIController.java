package org.example.parser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;

public class UIController {
    @FXML
    private ListView<String> lineNumbersListView;

    @FXML
    private TextArea contentTextArea;

    @FXML
    public void initialize() {
        setupLineNumbering();

    }

    private void setupLineNumbering() {
        updateLineNumbers(contentTextArea.getText());
        // Связываем изменения текста с обновлением номеров
        contentTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updateLineNumbers(newValue);
        });

        // Синхронизация прокрутки
        syncScrollBars();
    }

    private void updateLineNumbers(String text) {


        int lineCount = 1;
        if (!text.isEmpty()) {
            lineCount = text.split("\n").length;
        }

        if (text.endsWith("\n")) lineCount++;

        ObservableList<String> numbers = FXCollections.observableArrayList();
        for (int i = 1; i <= lineCount; i++) {
            numbers.add(String.format("%3d", i));
        }

        lineNumbersListView.setItems(numbers);
        syncSelection();
    }

    private void syncScrollBars() {
        // Синхронизация прокрутки через ScrollBar
        ScrollBar textAreaScrollBar = getVerticalScrollBar(contentTextArea);
        ScrollBar listViewScrollBar = getVerticalScrollBar(lineNumbersListView);

        if (textAreaScrollBar != null && listViewScrollBar != null) {
            textAreaScrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                listViewScrollBar.setValue(newVal.doubleValue());
            });
        }
    }

    private void syncSelection() {
        // Синхронизация текущей строки
        contentTextArea.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            int caretPos = newVal.intValue();
            String text = contentTextArea.getText();
            int lineNumber = 1;

            if (caretPos > 0 && text.length() > 0) {
                String textBeforeCaret = text.substring(0, Math.min(caretPos, text.length()));
                lineNumber = textBeforeCaret.split("\n", -1).length;
            }

            // Выделяем текущую строку в ListView
            if (lineNumber > 0 && lineNumber <= lineNumbersListView.getItems().size()) {
                lineNumbersListView.getSelectionModel().select(lineNumber - 1);
                lineNumbersListView.scrollTo(lineNumber - 1);
            }
        });
    }

    // Вспомогательный метод для получения ScrollBar
    private ScrollBar getVerticalScrollBar(Control control) {
        for (Node node : control.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation() == Orientation.VERTICAL) {
                return (ScrollBar) node;
            }
        }
        return null;
    }

}
