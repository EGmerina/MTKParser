package org.example.parser;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class UIController {
    @FXML
    private ListView<String> lineNumbersListView;

    @FXML
    private TextArea contentTextArea;

    @FXML
    private Slider fontSizeSlider;

    @FXML
    private Parent root;

    @FXML
    MenuItem close, save, clear, open;

    @FXML
    MenuItem language, grammar;


    @FXML
    public void initialize() {
//        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
//            updateFontSize(newVal.intValue());
//        });
        setupLineNumbering();
        setupMenuBar();

    }

    private void setupMenuBar() {
        close.setOnAction(event -> {
            Platform.exit();
        });
        clear.setOnAction(event -> {
            contentTextArea.clear();
        });
        open.setOnAction(event -> {
            openFile(event);
        });
        save.setOnAction(event -> {
            saveFile(event);
        });
        language.setOnAction(event -> {
            openLanguageFile();
        });
        grammar.setOnAction(event -> {
            openGrammarFile();
        });
    }

    private void openGrammarFile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/parser/description-view.fxml"));
            Parent root = loader.load();
            DescriptionController controller = loader.getController();
            controller.loadTextFromFile("grammar.txt");
            Stage stage = new Stage();
            stage.setTitle("G");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openLanguageFile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/parser/description-view.fxml"));
            Parent root = loader.load();
            DescriptionController controller = loader.getController();
            controller.loadTextFromFile("language.txt");
            Stage stage = new Stage();
            stage.setTitle("L");
            stage.setScene(new Scene(root, 100, 600));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save file");
        fileChooser.setInitialFileName("file.txt");
        Window window = contentTextArea.getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                Files.writeString(file.toPath(), contentTextArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Saving error");
                alert.setHeaderText("can't save the file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open txt file");

        Window window = contentTextArea.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            String content = null;
            try {
                content = Files.readString(Paths.get(selectedFile.toURI()));
                contentTextArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("can't open file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

//    private void updateFontSize(int size) {
//        // Устанавливаем размер через inline CSS
//        root.setStyle("-fx-font-size: " + size + "px;");
//
//        // Или добавляем/удаляем CSS классы
//        root.getStyleClass().removeAll("font-small", "font-medium", "font-large");
//
//        if (size < 12) {
//            root.getStyleClass().add("font-small");
//        } else if (size < 18) {
//            root.getStyleClass().add("font-medium");
//        } else {
//            root.getStyleClass().add("font-large");
//        }
//    }

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
            // Самый быстрый способ для больших текстов
            int newLineCount = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '\n') {
                    newLineCount++;
                }
            }
            lineCount = newLineCount + 1;

        }

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
