package org.example.parser.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private final ArrayList<Token> errorTokens = new ArrayList<>();

    // Синхронизационные множества для метода Айронса
    private final Set<TokenType> syncW = Set.of(TokenType.EOF);
    private final Set<TokenType> syncC = Set.of(TokenType.RPAREN, TokenType.LBRACE);
    private final Set<TokenType> syncE = Set.of(TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncT = Set.of(TokenType.ADD, TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncO = Set.of(TokenType.ADD, TokenType.MULTIPLY, TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncSL = Set.of(TokenType.RBRACE);
    private final Set<TokenType> syncS = Set.of(TokenType.RBRACE, TokenType.SEMICOLON);

    public Parser(String string) {
        this.lexer = new Lexer(string);
        this.currentToken = lexer.nextToken();
    }

    private void eat(TokenType expectedType) {
//        if (currentToken.getType() == expectedType) {
//            currentToken = lexer.nextToken();
//        } else {
//            errorTokens.add(currentToken);
//            // Метод Айронса: пропускаем неожиданный токен
//            currentToken = lexer.nextToken();
//        }
        currentToken = lexer.nextToken();
    }

    // Метод Айронса: пропуск до синхронизационного множества
    private void syncTo(Set<TokenType> syncSet) {
        while (!syncSet.contains(currentToken.getType()) && currentToken.getType() != TokenType.EOF) {
            errorTokens.add(currentToken);
            currentToken = lexer.nextToken();
        }
    }

    // Восстановление после ошибки с попыткой продолжить разбор
    private boolean attemptRecovery(Set<TokenType> syncSet, String errorMessage) {
        errorTokens.add(currentToken);
        System.err.println("Recovery: " + errorMessage + ", skipping to sync set");
        syncTo(syncSet);
        return false;
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
            Token operator = currentToken;
            eat(operator.getType());

            parseE();
        } catch (Exception e) {
            attemptRecovery(syncC, "Unexpected error in condition: " + e.getMessage());
        }
    }

    // <SL> → <S> <SL> | ε
    private void parseSL() {
        while (currentToken.getType() != TokenType.RBRACE && currentToken.getType() != TokenType.EOF) {
            parseS();
            // Если после ошибки в statement мы застряли, пропускаем до следующего оператора
            if (currentToken.getType() == TokenType.SEMICOLON) {
                eat(TokenType.SEMICOLON);
            }
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
            parseVAR();

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
            parseA(); // Добавляем вызов parseA для обработки аддитивных операций
        } catch (Exception e) {
            attemptRecovery(syncE, "Unexpected error in expression: " + e.getMessage());
        }
    }

    // <A> → + <T> <A> | - <T> <A> | ε
    private void parseA() {
        try {
            while (currentToken.getType() == TokenType.ADD) {
                Token operator = currentToken;
                eat(operator.getType());
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
            parseM(); // Добавляем вызов parseM для обработки мультипликативных операций
        } catch (Exception e) {
            attemptRecovery(syncT, "Unexpected error in term: " + e.getMessage());
        }
    }

    // <M> → * <O> <M> | / <O> <M> | ε
    private void parseM() {
        try {
            while (currentToken.getType() == TokenType.MULTIPLY) {
                Token operator = currentToken;
                eat(operator.getType());
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
                parseVAR();
            } else if (currentToken.getType() == TokenType.NUMBER) {
                parseNUM();
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

    // <VAR> → letter <VAR_TAIL>
    private void parseVAR() {
        if (currentToken.getType() != TokenType.VAR) {
            attemptRecovery(syncO, "Expected variable");
            return;
        }
        eat(TokenType.VAR);
    }

    // <NUM> → digit <NUM_TAIL>
    private void parseNUM() {
        if (currentToken.getType() != TokenType.NUMBER) {
            attemptRecovery(syncO, "Expected number");
            return;
        }
        eat(TokenType.NUMBER);
    }

    public String parse() {
        try {
            parseW();

            // Проверяем, есть ли лишние токены после разбора
            if (currentToken.getType() != TokenType.EOF) {
                errorTokens.add(currentToken);
                syncTo(syncW);
            }
        } catch (Exception e) {
            errorTokens.add(currentToken);
            System.err.println("Critical parser error: " + e.getMessage());
        }

        if (errorTokens.isEmpty()) {
            return "Time: " + LocalTime.now().getHour() + ":" +
                    String.format("%02d", LocalTime.now().getMinute()) + " [no error]\n";
        }

        StringBuilder errString = new StringBuilder();
        for (Token token : errorTokens) {
            errString.append("Time: ")
                    .append(LocalTime.now().getHour())
                    .append(":")
                    .append(String.format("%02d", LocalTime.now().getMinute()))
                    .append(" [error] ")
                    .append(token.getTokenDescription())
                    .append("\n");
        }

        return errString.toString();
    }

//    // Дополнительный метод для получения статистики
//    public String getParseSummary() {
//        return "Total tokens processed: " + lexer.getTokensProcessed() +
//                ", Errors found: " + errorTokens.size();
//    }
}