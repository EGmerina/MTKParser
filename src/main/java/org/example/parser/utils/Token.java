package org.example.parser.utils;

public class Token {
    private TokenType type;
    String tokenString;
    int line;
    int column;

    public Token(TokenType type, String tokenString, int line, int column) {
        this.type = type;
        this.tokenString = tokenString;
        this.line = line;
        this.column = column;

    }

    public String getTokenDescription() {
        return type + " (" + tokenString + ") at " + line + " : " + column;
    }

}
