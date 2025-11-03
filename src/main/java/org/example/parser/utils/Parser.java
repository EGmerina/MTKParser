package org.example.parser.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;
//TODO добавить в каждое множество while
public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private final ArrayList<String> errorMessages = new ArrayList<>();

    // Синхронизационные множества для метода Айронса
    private final Set<TokenType> syncW = Set.of(TokenType.WHILE, TokenType.EOF);
    private final Set<TokenType> syncC = Set.of(TokenType.RPAREN, TokenType.LBRACE);
    private final Set<TokenType> syncE = Set.of(TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncT = Set.of(TokenType.ADD, TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncO = Set.of(TokenType.ADD, TokenType.MULTIPLY, TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncSL = Set.of(TokenType.RBRACE, TokenType.WHILE);
    private final Set<TokenType> syncS = Set.of(TokenType.RBRACE, TokenType.SEMICOLON, TokenType.WHILE);

    public Parser(String string) {
        this.lexer = new Lexer(string);
        this.currentToken = lexer.nextToken();
    }

    private void eat(TokenType expectedType) {
        if (currentToken.getType() == expectedType) {
            currentToken = lexer.nextToken();
        } else {
            // Сохраняем информацию об ожидаемом и фактическом токене
            String expected = getTokenDescription(expectedType);
            String actual = currentToken.getTokenDescription();
            errorMessages.add("Expected " + expected + ", but found " + actual);

            // Метод Айронса: пропускаем неожиданный токен
            currentToken = lexer.nextToken();
        }
    }

    // Метод Айронса: пропуск до синхронизационного множества
    private void syncTo(Set<TokenType> syncSet, String context) {
        int skippedCount = 0;
        while (!syncSet.contains(currentToken.getType()) && currentToken.getType() != TokenType.EOF) {
            skippedCount++;
            currentToken = lexer.nextToken();
        }
        if (skippedCount > 0) {
            errorMessages.add("Skipped " + skippedCount + " tokens in " + context + ", recovered at " + getTokenDescription(currentToken.getType()));
        }
    }

    // Восстановление после ошибки с попыткой продолжить разбор
    private void attemptRecovery(Set<TokenType> syncSet, String errorMessage) {
        errorMessages.add(errorMessage + " (found " + currentToken.getTokenDescription() + ")");
        syncTo(syncSet, errorMessage);
    }

    // Вспомогательный метод для получения описания токена по типу
    private String getTokenDescription(TokenType type) {
        switch (type) {
            case WHILE: return "'while'";
            case LPAREN: return "'('";
            case RPAREN: return "')'";
            case LBRACE: return "'{'";
            case RBRACE: return "'}'";
            case SEMICOLON: return "';'";
            case ASSIGN: return "'='";
            case COMPARE: return "comparison operator";
            case ADD: return "'+' or '-'";
            case MULTIPLY: return "'*' or '/'";
            case VAR: return "identifier";
            case NUMBER: return "number";
            case EOF: return "end of input";
            default: return type.toString();
        }
    }

    // Главный метод для разбора всей программы
    public void parseProgram() {
        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.WHILE) {
                parseW();
            } else {
                // Если встретили не while, пытаемся восстановиться
                attemptRecovery(syncW, "Expected 'while' statement");
            }
        }
    }

    // <W> → while ( <C> ) { <SL> }
    public void parseW() {
        try {
            if (currentToken.getType() != TokenType.WHILE) {
                attemptRecovery(syncW, "Expected 'while'");
                return;
            }
            eat(TokenType.WHILE);

            if (currentToken.getType() != TokenType.LPAREN) {
                attemptRecovery(syncC, "Expected '(' after 'while'");
            } else {
                eat(TokenType.LPAREN);
            }

            parseC();

            if (currentToken.getType() != TokenType.RPAREN) {
                attemptRecovery(Set.of(TokenType.LBRACE), "Expected ')' after condition");
            } else {
                eat(TokenType.RPAREN);
            }

            if (currentToken.getType() != TokenType.LBRACE) {
                attemptRecovery(syncSL, "Expected '{' after condition");
            } else {
                eat(TokenType.LBRACE);
            }

            parseSL();

            if (currentToken.getType() != TokenType.RBRACE) {
                attemptRecovery(syncW, "Expected '}' after statements");
            } else {
                eat(TokenType.RBRACE);
            }

        } catch (Exception e) {
            attemptRecovery(syncW, "Unexpected error in while statement: " + e.getMessage());
        }
    }

    // <C> → <E> <CMP> <E>
    private void parseC() {
        try {
            parseE();

            if (currentToken.getType() != TokenType.COMPARE) {
                attemptRecovery(syncC, "Expected comparison operator");
                return;
            }
            eat(TokenType.COMPARE);

            parseE();
        } catch (Exception e) {
            attemptRecovery(syncC, "Unexpected error in condition: " + e.getMessage());
        }
    }

    // <SL> → <S> <SL> | ε
    private void parseSL() {
        while (currentToken.getType() != TokenType.RBRACE &&
                currentToken.getType() != TokenType.WHILE &&
                currentToken.getType() != TokenType.EOF) {
            parseS();
        }
    }

    // <S> → <AS> <END>
    private void parseS() {
        try {
            parseAS();

            if (currentToken.getType() != TokenType.SEMICOLON) {
                attemptRecovery(syncS, "Expected ';' after statement");
            } else {
                eat(TokenType.SEMICOLON);
            }
        } catch (Exception e) {
            attemptRecovery(syncS, "Unexpected error in statement: " + e.getMessage());
        }
    }

    // <AS> → <VAR> = <E>
    private void parseAS() {
        try {
            if (currentToken.getType() != TokenType.VAR) {
                attemptRecovery(syncE, "Expected variable in assignment");
                return;
            }
            eat(TokenType.VAR);

            if (currentToken.getType() != TokenType.ASSIGN) {
                attemptRecovery(syncE, "Expected '=' in assignment");
                return;
            }
            eat(TokenType.ASSIGN);

            parseE();
        } catch (Exception e) {
            attemptRecovery(syncE, "Unexpected error in assignment: " + e.getMessage());
        }
    }

    // <E> → <T> <A>
    private void parseE() {
        try {
            parseT();
            parseA();
        } catch (Exception e) {
            attemptRecovery(syncE, "Unexpected error in expression: " + e.getMessage());
        }
    }

    // <A> → + <T> <A> | - <T> <A> | ε
    private void parseA() {
        try {
            while (currentToken.getType() == TokenType.ADD) {
                eat(TokenType.ADD);
                parseT();
            }
        } catch (Exception e) {
            attemptRecovery(syncE, "Unexpected error in additive expression: " + e.getMessage());
        }
    }

    // <T> → <O> <M>
    private void parseT() {
        try {
            parseO();
            parseM();
        } catch (Exception e) {
            attemptRecovery(syncT, "Unexpected error in term: " + e.getMessage());
        }
    }

    // <M> → * <O> <M> | / <O> <M> | ε
    private void parseM() {
        try {
            while (currentToken.getType() == TokenType.MULTIPLY) {
                eat(TokenType.MULTIPLY);
                parseO();
            }
        } catch (Exception e) {
            attemptRecovery(syncT, "Unexpected error in multiplicative expression: " + e.getMessage());
        }
    }

    // <O> → <VAR> | <NUM> | ( <E> )
    private void parseO() {
        try {
            if (currentToken.getType() == TokenType.VAR) {
                eat(TokenType.VAR);
            } else if (currentToken.getType() == TokenType.NUMBER) {
                eat(TokenType.NUMBER);
            } else if (currentToken.getType() == TokenType.LPAREN) {
                eat(TokenType.LPAREN);
                parseE();
                if (currentToken.getType() != TokenType.RPAREN) {
                    attemptRecovery(syncO, "Expected ')' after expression");
                } else {
                    eat(TokenType.RPAREN);
                }
            } else {
                attemptRecovery(syncO, "Expected variable, number or '('");
            }
        } catch (Exception e) {
            attemptRecovery(syncO, "Unexpected error in operand: " + e.getMessage());
        }
    }

    public String parse() {
        try {
            parseProgram(); // Разбираем всю программу, а не один while

            // Проверяем, есть ли лишние токены после разбора
            if (currentToken.getType() != TokenType.EOF) {
                errorMessages.add("Unexpected tokens after program: " + currentToken.getTokenDescription());
            }
        } catch (Exception e) {
            errorMessages.add("Critical parser error: " + e.getMessage());
        }

        if (errorMessages.isEmpty()) {
            return "Time: " + LocalTime.now().getHour() + ":" +
                    String.format("%02d", LocalTime.now().getMinute()) + " [no error]\n";
        }

        StringBuilder errString = new StringBuilder();
        for (String error : errorMessages) {
            errString.append("Time: ")
                    .append(LocalTime.now().getHour())
                    .append(":")
                    .append(String.format("%02d", LocalTime.now().getMinute()))
                    .append(" [error] ")
                    .append(error)
                    .append("\n");
        }

        return errString.toString();
    }
}