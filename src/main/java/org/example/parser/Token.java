package org.example.parser;

public class Token {
    private TokenType type;
    String tokenString;
    int line;

    public Token(TokenType type, String tokenString, int line) {
        this.type = type;
        this.tokenString = tokenString;
        this.line = line;
    }

    public String getTokenDescription() {
        return type + " (" + tokenString + ") at " + line;
    }

}
