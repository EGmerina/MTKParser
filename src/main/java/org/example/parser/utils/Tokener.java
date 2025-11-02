package org.example.parser.utils;

import java.util.ArrayList;

public class Tokener {
    private String sourceString;
    private int currentSymbolNum = 0;
    private int column = 1;
    private int line = 1;

    public Tokener(String sourceString) {
        this.sourceString = sourceString;
    }

    public ArrayList<Token> getTokens() {
        ArrayList<Token> tokens = new ArrayList<>();
        while (currentSymbolNum < sourceString.length()) {
            //tokens.add(scanToken());
        }
        return tokens;
    }

//    private Token scanToken() {
//        char c = nextSymbol();
//        switch (c){
//            case '+': return new Token(TokenType.ADD, ) break;
//            case '-': addToken(TokenType.MINUS); break;
//            case '*': addToken(TokenType.STAR); break;
//            case '/': addToken(TokenType.SLASH); break;
//            case '=': addToken(TokenType.ASSIGN); break;
//            case '(': addToken(TokenType.LPAREN); break;
//            case ')': addToken(TokenType.RPAREN); break;
//            case '{': addToken(TokenType.LBRACE); break;
//            case '}': addToken(TokenType.RBRACE); break;
//            case ';': addToken(TokenType.SEMICOLON); break;
//            case ',': addToken(TokenType.COMMA); break;
//
//            case ' ':
//            case '\r':
//            case '\t':
//                column++; // просто пропускаем пробелы
//                break;
//
//            case '\n':
//                line++;
//                column = 1;
//                break;
//
//
//        }
//
//    }

    private char nextSymbol() {
        currentSymbolNum++;
        column++;
        return sourceString.charAt(currentSymbolNum - 1);
    }
}
