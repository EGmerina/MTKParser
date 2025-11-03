package org.example.parser;

import javafx.scene.control.TextArea;

public class TextChangeCommand implements Command {
    private final TextArea targetArea;
    private final String oldText;
    private final String newText;

    public TextChangeCommand(TextArea targetArea, String oldText, String newText) {
        this.targetArea = targetArea;
        this.oldText = oldText;
        this.newText = newText;
    }

    @Override
    public void execute() {
        // Устанавливаем новый текст
        targetArea.setText(newText);
    }

    @Override
    public void undo() {
        // Отменяем, возвращая старый текст
        targetArea.setText(oldText);
    }
}
