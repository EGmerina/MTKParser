package org.example.parser.utils;

import java.util.ArrayList;

public class Lexer {
    private final String input;
    private int line;
    private int column;
    private int position;
    private final int length;

    public Lexer(String input) {
        this.input = input;
        this.line = 1;
        this.column = 1;
        this.position = 0;
        this.length = input.length();
    }

    private char currentChar() {
        return position < length ? input.charAt(position) : '\0';
    }

    private void advance() {
        column++;
        position++;
    }

    private void skipWhitespace() {
        while (position < length && Character.isWhitespace(currentChar())) {
            if (currentChar() == '\n') {
                column = 1;
                line++;
            }
            advance();
        }
    }

    public Token nextToken() {
        skipWhitespace();

        if (position >= length) {
            return new Token(TokenType.EOF, "", line, column);
        }

        char current = currentChar();

        if (Character.isLetter(current)) {
            return readIdentifierOrKeyword();
        }

        if (Character.isDigit(current)) {
            return readNumber();
        }

        int startLine = line;
        int startCol = column;
        switch (current) {
            case '(':
                advance();
                return new Token(TokenType.LPAREN, "(", startLine, startCol);
            case ')':
                advance();
                return new Token(TokenType.RPAREN, ")", startLine, startCol);
            case '{':
                advance();
                return new Token(TokenType.LBRACE, "{", startLine, startCol);
            case '}':
                advance();
                return new Token(TokenType.RBRACE, "}", startLine, startCol);
            case ';':
                advance();
                return new Token(TokenType.SEMICOLON, ";", startLine, startCol);
            case '=':
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.COMPARE, "==", startLine, startCol);
                }
                return new Token(TokenType.ASSIGN, "=", startLine, startCol);
            case '+':
                advance();
                return new Token(TokenType.ADD, "+",  startLine, startCol);
            case '-':
                advance();
                return new Token(TokenType.ADD, "-",  startLine, startCol);
            case '*':
                advance();
                return new Token(TokenType.MULTIPLY, "*",  startLine, startCol);
            case '/':
                advance();
                return new Token(TokenType.MULTIPLY, "/",  startLine, startCol);
            case '<':
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.COMPARE, "<=",  startLine, startCol);
                }
                return new Token(TokenType.COMPARE, "<",  startLine, startCol);
            case '>':
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.COMPARE, ">=",  startLine, startCol);
                }
                return new Token(TokenType.COMPARE, ">",  startLine, startCol);
            case '!':
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.COMPARE, "!=",  startLine, startCol);
                }
                throw new RuntimeException("Unexpected character: ! at position " + (position - 1));
            default:
                throw new RuntimeException("Unexpected character: " + current + " at position " + position);
        }
    }

    private Token readIdentifierOrKeyword() {
        int start = position;
        int startLine = line;
        int startCol = column;
        while (position < length && (Character.isLetterOrDigit(currentChar()))) {
            advance();
        }

        String value = input.substring(start, position);

        if ("while".equals(value)) {
            return new Token(TokenType.WHILE, value, startLine, startCol);
        }

        return new Token(TokenType.VAR, value, startLine, startCol);
    }

    private Token readNumber() {
        int start = position;
        int startLine = line;
        int startCol = column;
        while (position < length && Character.isDigit(currentChar())) {
            advance();
        }
        String value = input.substring(start, position);
        return new Token(TokenType.NUMBER, value, startLine, startCol);
    }
}