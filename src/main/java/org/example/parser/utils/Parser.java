package org.example.parser.utils;

import java.time.LocalTime;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private final ArrayList<Token> errorTokens = new ArrayList<>();

    public Parser(String string) {
        this.lexer = new Lexer(string);
        this.currentToken = lexer.nextToken();
    }

    private void eat(TokenType expectedType) {
        if (currentToken.getType() == expectedType) {
            currentToken = lexer.nextToken();
        } else {
            errorTokens.add(currentToken);
            // throw new RuntimeException("Expected " + expectedType + ", but found " + currentToken.getType());
        }
    }

    // <W> → while ( <C> ) { <SL> }
    public void parseW() {
        eat(TokenType.WHILE);
        eat(TokenType.LPAREN);
        parseC();
        eat(TokenType.RPAREN);
        eat(TokenType.LBRACE);
        parseSL();
        eat(TokenType.RBRACE);
    }

    // <C> → <E> <CMP> <E>
    private void parseC() {
        parseE();
        Token operator = currentToken;

        // Проверяем, что токен является оператором сравнения
//        if (!isComparisonOperator(operator)) {
//            throw new RuntimeException("Expected comparison operator, but found " + operator.getType());
//        }
        eat(operator.getType());

        parseE();
    }

//    private boolean isComparisonOperator(Token token) {
//        return token.getType() == TokenType.COMPARE;
//    }

    // <SL> → <S> <SL> | ε
    private void parseSL() {
        while (currentToken.getType() != TokenType.RBRACE && currentToken.getType() != TokenType.EOF) {
            parseS();
        }
    }

    // <S> → <AS> <END>
    private void parseS() {
        parseAS();
        eat(TokenType.SEMICOLON);

    }

    // <AS> → <VAR> = <E>
    private void parseAS() {
        parseVAR();
        eat(TokenType.ASSIGN);
        parseE();

    }

    // <E> → <T> <A>
    private void parseE() {
        parseT();

    }

    // <A> → + <T> <A> | - <T> <A> | ε
    private void parseA() {
        while (currentToken.getType() == TokenType.ADD) {
            Token operator = currentToken;
            eat(operator.getType());
            parseT();

        }
    }

    // <T> → <O> <M>
    private void parseT() {
        parseO();
    }

    // <M> → * <O> <M> | / <O> <M> | ε
    private void parseM() {
        while (currentToken.getType() == TokenType.MULTIPLY) {
            Token operator = currentToken;
            eat(operator.getType());
            parseO();

        }

    }

    // <O> → <VAR> | <NUM> | ( <E> )
    private void parseO() {
        if (currentToken.getType() == TokenType.VAR) {
            parseVAR();
        } else if (currentToken.getType() == TokenType.NUMBER) {
            parseNUM();
        } else if (currentToken.getType() == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            parseE();
            eat(TokenType.RPAREN);

        } else {
            errorTokens.add(currentToken);
            // throw new RuntimeException("Expected identifier, number or '(', but found " + currentToken.getType());
        }
    }

    // <VAR> → letter <VAR_TAIL>
    private void parseVAR() {
//        if (currentToken.getType() != TokenType.VAR) {
//            throw new RuntimeException("Expected identifier, but found " + currentToken.getType());
//        }
        eat(TokenType.VAR);
    }

    // <NUM> → digit <NUM_TAIL>
    private void parseNUM() {
//        if (currentToken.getType() != TokenType.NUMBER) {
//            throw new RuntimeException("Expected number, but found " + currentToken.getType());
//        }

        eat(TokenType.NUMBER);

    }


    public String parse() {
        parseW();
        if (errorTokens.isEmpty()) {
            return "Time: " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + " [no error]\n";
        }

        String errString = new String();
        for (Token token : errorTokens) {
            errString += ("Time: " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + " [error] " + token.getTokenDescription() + "\n");
        }

        return errString;
    }
}


