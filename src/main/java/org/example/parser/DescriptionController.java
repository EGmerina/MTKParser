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
            textArea.setStyle("-fx-font-family: 'Arial'; -fx-font-size: " + 20 + "px;");
            String content = new String(getClass().getResourceAsStream(file).readAllBytes());
            textArea.setText(content);
        } catch (IOException e) {
            textArea.setText("Ошибка загрузки ресурса: " + e.getMessage());
        }
    }

    public void setText(String string){
        textArea.setText(string);

    }
}
