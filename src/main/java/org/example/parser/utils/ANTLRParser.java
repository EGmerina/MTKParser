package org.example.parser.utils;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.example.parser.gen.org.example.parser.WhileGrammarBaseListener;
import org.example.parser.gen.org.example.parser.WhileGrammarLexer;
import org.example.parser.gen.org.example.parser.WhileGrammarParser;

import java.util.ArrayList;
import java.util.List;

public class ANTLRParser {

    public static class SyntaxError {
        public final int line;
        public final int position;
        public final String message;

        public SyntaxError(int line, int position, String message) {
            this.line = line;
            this.position = position;
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("line %d, column %d - %s", line, position, message);
        }
    }

    public static class ParseResult {
        public final boolean success;
        public final List<SyntaxError> errors;
        public final String parseTree;

        public ParseResult(boolean success, List<SyntaxError> errors, String parseTree) {
            this.success = success;
            this.errors = errors;
            this.parseTree = parseTree;
        }
    }

    public ParseResult parse(String input) {
        List<SyntaxError> errors = new ArrayList<>();

        try {

            CharStream charStream = CharStreams.fromString(input);
            WhileGrammarLexer lexer = new WhileGrammarLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            WhileGrammarParser parser = new WhileGrammarParser(tokens);


            parser.removeErrorListeners();
            lexer.removeErrorListeners();

            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    errors.add(new SyntaxError(line, charPositionInLine, msg));
                }
            });

            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    errors.add(new SyntaxError(line, charPositionInLine, "Lexer: " + msg));
                }
            });


            ParseTree tree = parser.program();

            String treeString = tree.toStringTree(parser);

            return new ParseResult(errors.isEmpty(), errors, treeString);

        } catch (Exception e) {
            errors.add(new SyntaxError(0, 0, "ANTLR parsing failed: " + e.getMessage()));
            return new ParseResult(false, errors, "");
        }
    }


    public String parseAndFormat(String input) {
        ParseResult result = parse(input);

        StringBuilder output = new StringBuilder();

        if (result.success) {
            output.append(" success: No syntax errors found\n\n");
        } else {
            output.append(" errors: ").append(result.errors.size()).append(" syntax errors found\n");
            for (SyntaxError error : result.errors) {
                output.append("    ").append(error).append("\n");
            }
            output.append("\n");
        }
        return output.toString();
    }
}