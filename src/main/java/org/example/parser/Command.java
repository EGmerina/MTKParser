package org.example.parser;

public interface Command {
    void execute(); // Выполнить или повторить
    void undo();    // Отменить
}
