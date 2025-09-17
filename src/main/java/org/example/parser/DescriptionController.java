package org.example.parser;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;

public class DescriptionController {

    @FXML
    private TextArea textArea;

    public void loadTextFromFile(String file) {
        try {
            Font font = Font.font("Arial", FontWeight.NORMAL, 18); // размер 16px
            textArea.setFont(font);
            String content = new String(getClass().getResourceAsStream(file).readAllBytes());
            textArea.setText(content);
        } catch (IOException e) {
            textArea.setText("Ошибка загрузки ресурса: " + e.getMessage());
        }
    }
}
