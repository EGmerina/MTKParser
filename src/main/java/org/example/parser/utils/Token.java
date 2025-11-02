package org.example.parser.utils;

public class Token {
    private TokenType type;
    private String tokenString;
    private int line;
    private int column;

    public Token(TokenType type, String tokenString, int line, int column) {
        this.type = type;
        this.tokenString = tokenString;
        this.line = line;
        this.column = column;

    }

    public TokenType getType() {
        return type;
    }

    public String getTokenDescription() {
        return type + " " + tokenString + " at line " + line + ", symbol " + column;
    }

}
