package org.example.parser;

import java.util.Stack;

public class HistoryManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Очищаем Redo при новом действии
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command commandToUndo = undoStack.pop();
            commandToUndo.undo();
            redoStack.push(commandToUndo);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command commandToRedo = redoStack.pop();
            commandToRedo.execute();
            undoStack.push(commandToRedo);
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
